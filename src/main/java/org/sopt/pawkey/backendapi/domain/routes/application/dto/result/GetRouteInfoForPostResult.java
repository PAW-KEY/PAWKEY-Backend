package org.sopt.pawkey.backendapi.domain.routes.application.dto.result;

import java.util.List;

import lombok.Builder;

public record GetRouteInfoForPostResult(
	RouteDto routeDto,
	String petName
) {
	@Builder
	public record RouteDto(
		Long id,
		String locationDescription,
		String dateDescription,
		List<String> descriptionTags
	) {

	}

}
