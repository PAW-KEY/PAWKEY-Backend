package org.sopt.pawkey.backendapi.domain.routes.walk.api.dto.request;

import org.sopt.pawkey.backendapi.domain.routes.walk.application.dto.command.AppendWalkPointCommand;

public record WalkPointRequestDTO(String routeId,
								  double lat,
								  double lng,
								  long timestamp) {
	public AppendWalkPointCommand toCommand(Long userId) {
		return new AppendWalkPointCommand(userId,routeId, lat, lng, timestamp);
	}
}
