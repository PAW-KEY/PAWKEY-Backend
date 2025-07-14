package org.sopt.pawkey.backendapi.domain.routes.api.dto;

import org.sopt.pawkey.backendapi.domain.routes.application.dto.result.GetRouteInfoForPostResult;

import lombok.Builder;

public record GetRouteInfoForPostResponse(
	RouteDto routeDto,
	String petName
) {
	public static GetRouteInfoForPostResponse from(GetRouteInfoForPostResult result) {
		return new GetRouteInfoForPostResponse(
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
