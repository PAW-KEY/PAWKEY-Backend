package org.sopt.pawkey.backendapi.domain.routes.domain.repository;

import java.util.Optional;

import org.sopt.pawkey.backendapi.domain.routes.infra.persistence.entity.RouteEntity;

public interface RouteRepository {
	Optional<RouteEntity> getRouteByRouteId(Long routeId);

	RouteEntity save(RouteEntity route);
}
