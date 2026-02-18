package org.sopt.pawkey.backendapi.domain.routes.api.dto.response;

import java.util.Map;

import org.sopt.pawkey.backendapi.domain.routes.application.dto.result.GetSharedRouteMapDataResultDto;

import lombok.Builder;

@Builder
public record GetSharedRouteMapDataResponseDto(
	Long routeId,
	Map<String, Object> geometryDto

) {
	public static GetSharedRouteMapDataResponseDto from(GetSharedRouteMapDataResultDto resultDto) {
		return GetSharedRouteMapDataResponseDto.builder()
			.routeId(resultDto.routeId())
			.geometryDto(resultDto.geometryDto())
			.build();
	}
}
