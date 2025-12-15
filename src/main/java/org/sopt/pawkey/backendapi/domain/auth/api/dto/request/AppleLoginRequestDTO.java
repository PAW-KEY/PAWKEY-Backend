package org.sopt.pawkey.backendapi.domain.auth.api.dto.request;

public record AppleLoginRequestDTO( String authorizationCode,
									String deviceId) {
}
