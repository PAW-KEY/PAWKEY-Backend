package org.sopt.pawkey.backendapi.domain.routes.walk.api.dto.request;

import org.sopt.pawkey.backendapi.domain.routes.walk.application.dto.command.StartWalkCommand;

public record WalkStartRequestDTO(String deviceInfo) {
	public StartWalkCommand toCommand(Long userId) {
		return new StartWalkCommand(userId, deviceInfo);
	}
}
