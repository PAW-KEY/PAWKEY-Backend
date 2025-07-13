package org.sopt.pawkey.backendapi.domain.routes.application.dto.result;

import lombok.Builder;

public record GetRouteTrackingInfoResult(
	RouteDto routeDto,
	String petName
) {
	@Builder
	public record RouteDto(
		Long id,
		String locationDescription,
		String dateDescription
	) {

	}

}
