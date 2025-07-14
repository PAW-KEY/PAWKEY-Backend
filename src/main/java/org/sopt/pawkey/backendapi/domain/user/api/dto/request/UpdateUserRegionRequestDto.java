package org.sopt.pawkey.backendapi.domain.user.api.dto.request;

import org.sopt.pawkey.backendapi.domain.user.application.dto.request.UpdateUserRegionCommand;

import jakarta.validation.constraints.NotNull;

public record UpdateUserRegionRequestDto(
	@NotNull(message = "지역 아이디는 필수입니다.") Long regionId
) {
	public UpdateUserRegionCommand toCommand() {
		return new UpdateUserRegionCommand(regionId());
	}
}
