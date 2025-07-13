package org.sopt.pawkey.backendapi.domain.routes.domain.repository;

import org.sopt.pawkey.backendapi.domain.routes.infra.persistence.entity.RouteEntity;

public interface RouteRepository {
	RouteEntity save(RouteEntity route);
}
