package org.sopt.pawkey.backendapi.global.auth.api.dto.request;

import jakarta.validation.constraints.NotBlank;

public record LogoutRequestDTO(
	@NotBlank(message = "기기 식별자(deviceId)는 필수입니다.")
	String deviceId
) {
}