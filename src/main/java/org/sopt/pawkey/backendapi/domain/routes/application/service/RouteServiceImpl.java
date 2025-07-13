package org.sopt.pawkey.backendapi.domain.routes.application.service;

import java.util.Optional;

import org.sopt.pawkey.backendapi.domain.routes.domain.RouteRepository;
import org.sopt.pawkey.backendapi.domain.routes.exception.RouteBusinessException;
import org.sopt.pawkey.backendapi.domain.routes.exception.RouteErrorCode;
import org.sopt.pawkey.backendapi.domain.routes.infra.persistence.entity.RouteEntity;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RouteServiceImpl implements RouteService{
	private final RouteRepository routeRepository;
	@Override
	public RouteEntity getRouteById(Long routeId) {
		return routeRepository.getRouteByRouteId(routeId).orElseThrow(()-> new RouteBusinessException(RouteErrorCode.ROUTE_NOT_FOUND));
	}
}
