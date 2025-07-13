package org.sopt.pawkey.backendapi.domain.routes.infra.persistence;

import org.sopt.pawkey.backendapi.domain.routes.domain.repository.RouteRepository;
import org.sopt.pawkey.backendapi.domain.routes.infra.persistence.entity.RouteEntity;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class RouteRepositoryImpl implements RouteRepository {
	private final SpringDataRouteRepository jpaRepository;

	@Override
	public RouteEntity save(RouteEntity route) {
		return jpaRepository.save(route);
	}
}
