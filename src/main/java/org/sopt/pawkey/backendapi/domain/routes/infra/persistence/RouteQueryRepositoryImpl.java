package org.sopt.pawkey.backendapi.domain.routes.infra.persistence;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.sopt.pawkey.backendapi.domain.homeWeather.api.dto.HomeInfoResponseDto;
import org.sopt.pawkey.backendapi.domain.routes.infra.persistence.entity.QRouteEntity;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class RouteQueryRepositoryImpl implements RouteQueryRepository {
	private final JPAQueryFactory query;
	private final QRouteEntity route = QRouteEntity.routeEntity;

	@Override
	public HomeInfoResponseDto getMonthlyWalkSummary(Long userId) {
		LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
		LocalDateTime startOfMonth = LocalDateTime.of(now.getYear(), now.getMonth(), 1, 0, 0, 0);

		return query.select(Projections.constructor(HomeInfoResponseDto.class,
				route.distance.sum().doubleValue().divide(1000.0).as("distance"),
				route.duration.sum().as("totalTime"),
				route.count().intValue().as("count")
			))
			.from(route)
			.where(
				route.user.userId.eq(userId),
				route.createdAt.goe(startOfMonth)
			)
			.fetchOne();
	}
}