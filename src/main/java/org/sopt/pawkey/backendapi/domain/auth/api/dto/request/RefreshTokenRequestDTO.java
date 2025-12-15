package org.sopt.pawkey.backendapi.domain.auth.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record RefreshTokenRequestDTO(
	@Schema(description = "만료된 Access Token 대신 사용하는 Refresh Token", example = "eyJhbGciOiJIUzI1NiIs...")
	@NotBlank(message = "Refresh Token은 필수 입력 값입니다.")
	String refreshToken,

	@Schema(description = "로그인 시 사용된 클라이언트의 고유 기기 ID", example = "abcdef1234567890")
	@NotBlank(message = "기기 ID는 필수 입력 값입니다.")
	String deviceId) {
}
