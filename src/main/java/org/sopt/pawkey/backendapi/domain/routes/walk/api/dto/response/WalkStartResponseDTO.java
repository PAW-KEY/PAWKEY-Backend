package org.sopt.pawkey.backendapi.domain.routes.walk.api.dto.response;

import org.sopt.pawkey.backendapi.domain.routes.walk.application.dto.result.StartWalkResult;

public record WalkStartResponseDTO(String routeId, long issuedAt) {
	public static WalkStartResponseDTO from(StartWalkResult result) {
		return new WalkStartResponseDTO(result.routeId(), result.issuedAt());
	}
}
