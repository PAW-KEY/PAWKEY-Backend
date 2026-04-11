package org.sopt.pawkey.backendapi.domain.routes.application.facade.query;

import lombok.RequiredArgsConstructor;
import org.sopt.pawkey.backendapi.domain.routes.application.dto.command.GetRouteGeometryCommand;
import org.sopt.pawkey.backendapi.domain.routes.application.dto.result.GetRouteGeometryResult;
import org.sopt.pawkey.backendapi.domain.routes.application.service.RouteService;
import org.sopt.pawkey.backendapi.domain.routes.infra.persistence.entity.RouteEntity;
import org.sopt.pawkey.backendapi.domain.user.application.service.UserService;
import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetRouteGeometryFacade {

    private final RouteService routeService;
    private final UserService userService;

    public GetRouteGeometryResult execute(Long userId, GetRouteGeometryCommand commandDto) {
        UserEntity user = userService.findById(userId);
        RouteEntity route = routeService.getRouteById(commandDto.routeId());

        return GetRouteGeometryResult.from(route);
    }
}

