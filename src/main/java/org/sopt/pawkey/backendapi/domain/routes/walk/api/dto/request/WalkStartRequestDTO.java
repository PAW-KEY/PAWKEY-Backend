package org.sopt.pawkey.backendapi.domain.routes.walk.api.dto.request;

import org.sopt.pawkey.backendapi.domain.routes.walk.application.dto.command.StartWalkCommand;

public record WalkStartRequestDTO(Long userId,
								  String deviceInfo) {
	public StartWalkCommand toCommand() {
		return new StartWalkCommand(userId, deviceInfo);
	}
}
