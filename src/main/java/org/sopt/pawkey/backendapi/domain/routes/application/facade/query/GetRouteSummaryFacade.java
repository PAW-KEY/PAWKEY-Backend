package org.sopt.pawkey.backendapi.domain.routes.application.facade.query;

import lombok.RequiredArgsConstructor;
import org.sopt.pawkey.backendapi.domain.routes.application.dto.command.GetRouteSummaryCommand;
import org.sopt.pawkey.backendapi.domain.routes.application.dto.result.GetRouteSummaryResult;
import org.sopt.pawkey.backendapi.domain.routes.application.service.RouteService;
import org.sopt.pawkey.backendapi.domain.routes.infra.persistence.entity.RouteEntity;
import org.sopt.pawkey.backendapi.domain.user.application.service.UserService;
import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetRouteSummaryFacade {

    private final RouteService routeService;
    private final UserService userService;

    public GetRouteSummaryResult execute(Long userId, GetRouteSummaryCommand commandDto) {
        UserEntity user = userService.findById(userId);
        RouteEntity route = routeService.getRouteById(commandDto.routeId());

        return GetRouteSummaryResult.from(route);
    }
}
