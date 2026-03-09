package org.sopt.pawkey.backendapi.domain.routes.infra.persistence;

import java.util.List;
import java.util.Optional;

import org.sopt.pawkey.backendapi.domain.homeWeather.api.dto.HomeInfoResponseDto;
import org.sopt.pawkey.backendapi.domain.routes.domain.repository.RouteQueryRepository;
import org.sopt.pawkey.backendapi.domain.routes.domain.repository.RouteRepository;
import org.sopt.pawkey.backendapi.domain.routes.infra.persistence.entity.RouteEntity;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class RouteRepositoryImpl implements RouteRepository {
	private final SpringDataRouteRepository jpaRepository;

	@Override
	public Optional<RouteEntity> getRouteByRouteId(Long routeId) {
		return jpaRepository.getByRouteId(routeId);
	}

	@Override
	public RouteEntity save(RouteEntity route) {
		return jpaRepository.save(route);
	}

	private final RouteQueryRepository queryRepository;

	@Override
	public HomeInfoResponseDto getMonthlyWalkSummary(Long userId) {
		return queryRepository.getMonthlyWalkSummary(userId);
	}

	@Override
	public List<RouteEntity> findAllById(List<Long> ids) {
		if (ids == null || ids.isEmpty()) return List.of();

		return jpaRepository.findAllByIdIn(ids);
	}

	@Override
	public void deleteByUserId(Long userId) {
		jpaRepository.deleteByUserId(userId);
	}

	@Override
	public List<RouteEntity> findByUserUserId(Long userId) {
		return jpaRepository.findByUserUserId(userId);
	}


}