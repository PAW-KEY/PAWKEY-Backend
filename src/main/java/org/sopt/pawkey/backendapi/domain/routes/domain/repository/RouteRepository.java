package org.sopt.pawkey.backendapi.domain.routes.domain.repository;

import java.util.List;
import java.util.Optional;

import org.sopt.pawkey.backendapi.domain.homeWeather.api.dto.HomeInfoResponseDto;
import org.sopt.pawkey.backendapi.domain.routes.infra.persistence.entity.RouteEntity;

public interface RouteRepository {
	Optional<RouteEntity> getRouteByRouteId(Long routeId);

	RouteEntity save(RouteEntity route);

	HomeInfoResponseDto getMonthlyWalkSummary(Long userId);

	List<RouteEntity> findAllById(List<Long> ids);

	void deleteByUserId(Long userId);
}
