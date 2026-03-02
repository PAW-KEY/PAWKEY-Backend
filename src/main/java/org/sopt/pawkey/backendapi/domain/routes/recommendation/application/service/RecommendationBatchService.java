package org.sopt.pawkey.backendapi.domain.routes.recommendation.application.service;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sopt.pawkey.backendapi.domain.dbti.infra.persistence.entity.QDbtiResultEntity;
import org.sopt.pawkey.backendapi.domain.pet.infra.persistence.entity.QPetEntity;
import org.sopt.pawkey.backendapi.domain.post.infra.persistence.entity.QPostEntity;
import org.sopt.pawkey.backendapi.domain.post.infra.persistence.entity.QPostLikeEntity;
import org.sopt.pawkey.backendapi.domain.routes.recommendation.domain.repository.RouteRecoStatRepository;
import org.sopt.pawkey.backendapi.domain.routes.recommendation.infra.persistence.entity.RouteRecoStatEntity;
import org.sopt.pawkey.backendapi.domain.routes.infra.persistence.entity.QRouteEntity;
import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.QUserEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class RecommendationBatchService {
    private final JPAQueryFactory query;
    private final RouteRecoStatRepository statRepository;

    private final QRouteEntity route = QRouteEntity.routeEntity;
    private final QPostEntity post = QPostEntity.postEntity;
    private final QPostLikeEntity postLike = QPostLikeEntity.postLikeEntity;
    private final QUserEntity user = QUserEntity.userEntity;
    private final QPetEntity pet = QPetEntity.petEntity;
    private final QDbtiResultEntity dbtiResult = QDbtiResultEntity.dbtiResultEntity;

    @Scheduled(cron = "0 0 3 * * *")
    @Transactional
    public void refreshRouteRecoStats() {
        statRepository.deleteAllInBatch();

        List<RouteRecoStatEntity> newStats = query
                .select(Projections.constructor(
                        RouteRecoStatEntity.class,
                        Expressions.nullExpression(),
                        route.region.parent.regionId.coalesce(route.region.regionId),
                        dbtiResult.dbtiType,
                        route.routeId,
                        post.likeCount.max().coalesce(0L)
                ))
                .from(post)
                .join(post.route, route)
                .join(post.user, user)
                .join(user.petEntityList, pet)
                .join(pet.dbtiResult, dbtiResult)
                .where(post.isPublic.isTrue())
                .groupBy(
                        route.region.parent.regionId.coalesce(route.region.regionId),
                        dbtiResult.dbtiType,
                        route.routeId
                )
                .fetch();

        if (!newStats.isEmpty()) {
            statRepository.saveAll(newStats);
            log.info("✅ {}건의 추천 통계 데이터 갱신 완료", newStats.size());
        }
    }
}