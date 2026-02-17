package org.sopt.pawkey.backendapi.domain.routes.api.dto.response;

import java.util.List;

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
				.descriptionTags(result.routeDto().descriptionTags())
				.build(),
			result.petName()
		);
	}

	@Builder
	public record RouteDto(
		Long id,
		String locationDescription,
		String dateDescription,
		List<String> descriptionTags
	) {

	}
}
