package org.sopt.pawkey.backendapi.domain.routes.api.dto;

import org.sopt.pawkey.backendapi.domain.routes.application.dto.result.RouteRegisterResult;

public record RouteRegisterResponse(
	Long routeId
) {
	public static RouteRegisterResponse from(RouteRegisterResult result) {
		return new RouteRegisterResponse(result.routeId());
	}

}
