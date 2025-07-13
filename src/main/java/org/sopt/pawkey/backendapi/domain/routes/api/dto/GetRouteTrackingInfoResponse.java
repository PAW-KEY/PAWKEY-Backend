package org.sopt.pawkey.backendapi.domain.routes.api.dto;

import org.sopt.pawkey.backendapi.domain.routes.application.dto.result.GetRouteTrackingInfoResult;

import lombok.Builder;

public record GetRouteTrackingInfoResponse(
	RouteDto routeDto,
	String petName
) {
	public static GetRouteTrackingInfoResponse from(GetRouteTrackingInfoResult result) {
		return new GetRouteTrackingInfoResponse(
			RouteDto.builder()
				.id(result.routeDto().id())
				.locationDescription(result.routeDto().locationDescription())
				.dateDescription(result.routeDto().dateDescription())
				.build(),
			result.petName()
		);
	}

	@Builder
	public record RouteDto(
		Long id,
		String locationDescription,
		String dateDescription
	) {

	}
}
