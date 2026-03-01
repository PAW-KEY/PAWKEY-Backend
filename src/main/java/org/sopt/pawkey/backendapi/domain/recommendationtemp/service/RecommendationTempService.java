package org.sopt.pawkey.backendapi.domain.recommendationtemp.service;

import lombok.RequiredArgsConstructor;
import org.sopt.pawkey.backendapi.domain.dbti.domain.model.DbtiType;
import org.sopt.pawkey.backendapi.domain.recommendationtemp.dto.RouteScoreProjection;
import org.sopt.pawkey.backendapi.domain.recommendationtemp.dto.TempRecommendationResponseDto;
import org.sopt.pawkey.backendapi.domain.recommendationtemp.repository.RecommendationTempRepository;
import org.sopt.pawkey.backendapi.domain.routes.infra.persistence.SpringDataRouteRepository;
import org.sopt.pawkey.backendapi.domain.routes.infra.persistence.entity.RouteEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecommendationTempService {

    private final RecommendationTempRepository recommendationTempRepository;
    private final SpringDataRouteRepository springDataRouteRepository;

    public List<TempRecommendationResponseDto> getRecommendation(
            Long regionId,
            DbtiType dbtiType
    ) {

        // 1️⃣ 실시간 집계
        List<RouteScoreProjection> topRoutes =
                recommendationTempRepository.findTopRoutes(regionId, dbtiType);

        if (topRoutes.isEmpty()) {
            return List.of();
        }

        // 2️⃣ routeId 추출
        List<Long> routeIds = topRoutes.stream()
                .map(RouteScoreProjection::routeId)
                .toList();

        // 3️⃣ 상세 조회
        List<RouteEntity> routes =
                springDataRouteRepository.findAllById(routeIds);

        Map<Long, RouteEntity> routeMap =
                routes.stream().collect(Collectors.toMap(RouteEntity::getRouteId, r -> r));

        // 4️⃣ DTO 조립
        return topRoutes.stream()
                .map(score -> {
                    RouteEntity route = routeMap.get(score.routeId());
                    return TempRecommendationResponseDto.of(route, score.score());
                })
                .toList();
    }
}
