package org.sopt.pawkey.backendapi.domain.routes.application.service;

import java.util.Optional;

import org.sopt.pawkey.backendapi.domain.routes.infra.persistence.entity.RouteEntity;

public interface RouteService {
	RouteEntity getRouteById(Long routeId);
}
