package org.sopt.pawkey.backendapi.domain.routes.application.facade.query;

import org.sopt.pawkey.backendapi.domain.routes.application.dto.command.GetSharedRouteMapDataCommandDto;
import org.sopt.pawkey.backendapi.domain.routes.application.dto.result.GetSharedRouteMapDataResultDto;
import org.sopt.pawkey.backendapi.domain.routes.application.service.RouteService;
import org.sopt.pawkey.backendapi.domain.routes.infra.persistence.entity.RouteEntity;
import org.sopt.pawkey.backendapi.domain.user.application.service.UserService;
import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.UserEntity;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GetSharedRouteMapDataFacade {

	private final RouteService routeService;
	private final UserService userService;

	public GetSharedRouteMapDataResultDto execute(Long userId, GetSharedRouteMapDataCommandDto commandDto) {
		UserEntity user = userService.getByUserId(userId);
		RouteEntity route = routeService.getRouteById(commandDto.routeId());

		return GetSharedRouteMapDataResultDto.from(route);
	}
}
