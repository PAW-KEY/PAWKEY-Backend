package org.sopt.pawkey.backendapi.domain.routes.application.dto.result;


import org.sopt.pawkey.backendapi.domain.routes.infra.persistence.entity.RouteEntity;

import lombok.Builder;

public record RouteRegisterResult(
	Long routeId
) {
	public static RouteRegisterResult from(RouteEntity entity) {
		return new RouteRegisterResult(entity.getRouteId());
	}

}
