package org.sopt.pawkey.backendapi.domain.routes.api.dto.response;

import lombok.Builder;
import org.sopt.pawkey.backendapi.domain.routes.application.dto.result.GetRouteGeometryResult;

import java.util.Map;

@Builder
public record GetRouteGeometryResponseDto(
        Long routeId,
        Map<String, Object> geometry
) {
    public static GetRouteGeometryResponseDto from(GetRouteGeometryResult resultDto) {
        return GetRouteGeometryResponseDto.builder()
                .routeId(resultDto.routeId())
                .geometry(resultDto.geometryDto())
                .build();
    }
}