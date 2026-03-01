package org.sopt.pawkey.backendapi.domain.recommendationtemp.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.sopt.pawkey.backendapi.domain.dbti.domain.model.DbtiType;
import org.sopt.pawkey.backendapi.domain.dbti.infra.persistence.entity.QDbtiResultEntity;
import org.sopt.pawkey.backendapi.domain.pet.infra.persistence.entity.QPetEntity;
import org.sopt.pawkey.backendapi.domain.post.infra.persistence.entity.QPostEntity;
import org.sopt.pawkey.backendapi.domain.recommendationtemp.dto.RouteScoreProjection;
import org.sopt.pawkey.backendapi.domain.routes.infra.persistence.entity.QRouteEntity;
import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.QUserEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class RecommendationTempRepositoryImpl implements RecommendationTempRepository {

    private final JPAQueryFactory query;

    private final QRouteEntity route = QRouteEntity.routeEntity;
    private final QPostEntity post = QPostEntity.postEntity;
    private final QUserEntity user = QUserEntity.userEntity;
    private final QPetEntity pet = QPetEntity.petEntity;
    private final QDbtiResultEntity dbti = QDbtiResultEntity.dbtiResultEntity;

    @Override
    public List<RouteScoreProjection> findTopRoutes(
            Long regionId,
            DbtiType dbtiType
    ) {

        NumberExpression<Long> scoreExpression =
                post.count()
                        .add(post.likeCount.sum().coalesce(0L));

        return query
                .select(Projections.constructor(
                        RouteScoreProjection.class,
                        route.routeId,
                        scoreExpression
                ))
                .from(route)
                .join(post).on(post.route.eq(route))
                .join(route.user, user)
                .join(user.petEntityList, pet)
                .join(pet.dbtiResult, dbti)
                .where(
                        route.region.regionId.eq(regionId),
                        dbti.dbtiType.eq(dbtiType)
                )
                .groupBy(route.routeId)
                .orderBy(scoreExpression.desc())
                .limit(4)
                .fetch();
    }
}