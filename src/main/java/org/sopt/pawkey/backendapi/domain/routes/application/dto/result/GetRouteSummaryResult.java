package org.sopt.pawkey.backendapi.domain.routes.application.dto.result;

import org.sopt.pawkey.backendapi.domain.routes.infra.persistence.entity.RouteEntity;
import org.sopt.pawkey.backendapi.global.util.RouteTimeFormatter;

import java.util.List;

public record GetRouteSummaryResult(Long routeId,
                                    String locationText,
                                    String dateTimeText,
                                    List<String> metaTagTexts) {
    public static GetRouteSummaryResult from(RouteEntity route){
        return new GetRouteSummaryResult(
                route.getRouteId(),
                route.getRegion().getRegionName(),
                RouteTimeFormatter.format(route.getStartedAt()),
                List.of(
                        route.getDistance() / 1000.0 + "km",
                        route.getDurationMinutes() + "분",
                        route.getStepCount() + "걸음"
                )
        );
    }

}
