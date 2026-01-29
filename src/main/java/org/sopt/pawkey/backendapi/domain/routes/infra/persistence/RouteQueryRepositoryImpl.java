package org.sopt.pawkey.backendapi.domain.routes.infra.persistence;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.sopt.pawkey.backendapi.domain.home.api.dto.response.HomeInfoResponseDto;
import org.sopt.pawkey.backendapi.domain.routes.infra.persistence.entity.QRouteEntity;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RouteQueryRepositoryImpl implements RouteQueryRepository {
	private final JPAQueryFactory query;
	private final QRouteEntity route = QRouteEntity.routeEntity;

	@Override
	public HomeInfoResponseDto getMonthlyWalkSummary(Long userId) {
		LocalDateTime startOfMonth = LocalDate.now().withDayOfMonth(1).atStartOfDay();

		return query.select(Projections.constructor(HomeInfoResponseDto.class,
				route.distance.sum().doubleValue().coalesce(0.0), // 누적 거리
				route.duration.sum().coalesce(0),   // 총 산책 시간 (초)
				route.count().intValue()            // 산책 횟수
			))
			.from(route)
			.where(
				route.user.userId.eq(userId),
				route.createdAt.goe(startOfMonth)
			)
			.fetchOne();
	}
}