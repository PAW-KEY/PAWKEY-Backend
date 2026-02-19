package org.sopt.pawkey.backendapi.domain.routes.application.dto.result;

import org.sopt.pawkey.backendapi.domain.routes.infra.persistence.entity.RouteEntity;

import java.util.Map;

public record GetRouteGeometryResult(
        Long routeId,
        Map<String, Object> geometryDto) {
    public static GetRouteGeometryResult from(RouteEntity route){
        return new GetRouteGeometryResult(
                route.getRouteId(),
                route.getGeoJson());

    }
}
