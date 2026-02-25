package org.sopt.pawkey.backendapi.domain.routes.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import org.sopt.pawkey.backendapi.domain.routes.application.dto.result.GetRouteGeometryResult;

import java.util.Map;

@Builder
public record GetRouteGeometryResponseDto(
        Long routeId,
        @Schema(
                description = "GeoJSON LineString",
                example = """
            {
              "type": "LineString",
              "coordinates": [
                [126.97, 37.56],
                [127.02, 37.57],
                [127.05, 37.58]
              ]
            }
            """
        )
        Map<String, Object> geometry
) {
    public static GetRouteGeometryResponseDto from(GetRouteGeometryResult resultDto) {
        return GetRouteGeometryResponseDto.builder()
                .routeId(resultDto.routeId())
                .geometry(resultDto.geometryDto())
                .build();
    }
}