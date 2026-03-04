package org.sopt.pawkey.backendapi.domain.routes.recommendation.application.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sopt.pawkey.backendapi.domain.dbti.domain.model.DbtiType;
import org.sopt.pawkey.backendapi.domain.dbti.infra.persistence.entity.DbtiResultEntity;
import org.sopt.pawkey.backendapi.domain.pet.infra.persistence.entity.PetEntity;
import org.sopt.pawkey.backendapi.domain.post.domain.repository.PostRepository;
import org.sopt.pawkey.backendapi.domain.post.infra.persistence.entity.PostEntity;
import org.sopt.pawkey.backendapi.domain.routes.recommendation.application.dto.command.GetRecommendationCommand;
import org.sopt.pawkey.backendapi.domain.routes.recommendation.application.dto.result.RecommendationResult;
import org.sopt.pawkey.backendapi.domain.routes.recommendation.domain.repository.RouteRecoStatRepository;
import org.sopt.pawkey.backendapi.domain.routes.recommendation.infra.persistence.entity.RouteRecoStatEntity;
import org.sopt.pawkey.backendapi.domain.routes.domain.repository.RouteRepository;
import org.sopt.pawkey.backendapi.domain.routes.infra.persistence.entity.RouteEntity;
import org.sopt.pawkey.backendapi.domain.user.domain.repository.UserRepository;
import org.sopt.pawkey.backendapi.domain.user.exception.UserBusinessException;
import org.sopt.pawkey.backendapi.domain.user.exception.UserErrorCode;
import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.UserEntity;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecommendationService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final RouteRecoStatRepository statRepository;
    private final RouteRepository routeRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    private static final String KEY_PREFIX = "HOME_RECO:";

    public List<RecommendationResult> getRecommendations(GetRecommendationCommand command) {

        UserEntity user = userRepository.findById(command.userId())
                .orElseThrow(() -> new UserBusinessException(UserErrorCode.USER_NOT_FOUND));

        PetEntity myPet = user.getPetOrThrow();
        DbtiType myDbti = (myPet.getDbtiResult() != null) ? myPet.getDbtiResult().getDbtiType() : null;
        if (myDbti == null) return List.of();

        //구 단위(Parent) 지역 ID 추출
        Long parentRegionId = (user.getRegion().getParent() != null)
                ? user.getRegion().getParent().getRegionId() : user.getRegion().getRegionId();

        String cacheKey = KEY_PREFIX + parentRegionId + ":" + myDbti.name();

        //Set<Object> cachedRouteIds = redisTemplate.opsForZSet().reverseRange(cacheKey, 0, 3);
        Set<Object> cachedRouteIds = Set.of();
        try {
            Set<Object> redisIds = redisTemplate.opsForZSet().reverseRange(cacheKey, 0, 3);
            if (redisIds != null) {
                cachedRouteIds = redisIds;
                }
            } catch (Exception e) {
            log.warn("Redis read 실패=> RDB 폴백으로 진행합니다. key={}", cacheKey, e);
            }
        List<Long> routeIds;

        if (cachedRouteIds != null && !cachedRouteIds.isEmpty()) {
            routeIds = cachedRouteIds.stream()
                    .map(Object::toString)
                    .map(Long::valueOf)
                    .toList();
        } else {
            List<RouteRecoStatEntity> stats = statRepository.findTop4(parentRegionId, myDbti);
            if (stats.isEmpty()) return List.of();

            //stats.forEach(s -> redisTemplate.opsForZSet().add(cacheKey, s.getRouteId().toString(), s.getScore().doubleValue()));
            //redisTemplate.expire(cacheKey, Duration.ofHours(1));
            try {
                stats.forEach(s -> redisTemplate.opsForZSet()
                        .add(cacheKey, s.getRouteId().toString(), s.getScore().doubleValue()));
                redisTemplate.expire(cacheKey, Duration.ofHours(1));
                } catch (Exception e) {
                log.warn("Redis write 실패=>  캐시 저장은 건너뜁니다. key={}", cacheKey, e);
                }
            routeIds = stats.stream().map(RouteRecoStatEntity::getRouteId).toList();
        }

        // 상세 정보 로드
        List<RouteEntity> routes = routeRepository.findAllById(routeIds);

        // Post 정보 역조회 (인터페이스 메서드 사용)
        List<PostEntity> posts = postRepository.findAllByRouteIds(routeIds);

        Map<Long, PostEntity> postMap = posts.stream()
                .collect(Collectors.toMap(
                        p -> p.getRoute().getRouteId(),
                        p -> p,
                        (p1, p2) -> p1
                ));

        Map<Long, RouteEntity> routeMap = routes.stream()
                .collect(Collectors.toMap(RouteEntity::getRouteId, r -> r));

        return routeIds.stream()
                .filter(routeMap::containsKey)
                .map(id -> {
                    RouteEntity r = routeMap.get(id);
                    PostEntity post = postMap.get(id);

                    return RecommendationResult.of(
                            r,
                            post != null ? post.getLikeCount() : 0L,
                            post != null ? post.getPostId() : null
                    );
                })
                .toList();

    }
    @Transactional(readOnly = true)
    public void refreshRedisFromStats() {

        List<RouteRecoStatEntity> stats = statRepository.findAll();

        for (RouteRecoStatEntity stat : stats) {

            String key = "HOME_RECO:" + stat.getRegionId() + ":" + stat.getDbtiType();

            redisTemplate.opsForZSet()
                    .add(key, stat.getRouteId(), stat.getScore());
        }

        log.info("Redis 랭킹 적재 완료: {}건", stats.size());
    }
}
