package org.sopt.pawkey.backendapi.domain.routes.application.dto.result;

import java.util.Map;

import org.sopt.pawkey.backendapi.domain.routes.infra.persistence.entity.RouteEntity;

import lombok.Builder;

@Builder
public record GetSharedRouteMapDataResultDto(
	Long routeId,
	Map<String, Object> geometryDto
) {
	public static GetSharedRouteMapDataResultDto from(RouteEntity route) {
		return GetSharedRouteMapDataResultDto.builder()
			.routeId(route.getRouteId())
			.geometryDto(route.getGeoJson())
			.build();
	}
}
