package org.sopt.pawkey.backendapi.global.auth.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(@Schema(description = "소셜 플랫폼으로부터 받은 ID Token", example = "eyJhbGciOiJSUzI1NiIs...")
							  @NotBlank(message = "ID 토큰은 필수 입력 값입니다.")
							  String idToken,

							  @Schema(description = "로그인을 시도하는 클라이언트의 고유 기기 ID", example = "abcdef1234567890")
							  @NotBlank(message = "기기 ID는 필수 입력 값입니다.")
							  String deviceId
) {
}