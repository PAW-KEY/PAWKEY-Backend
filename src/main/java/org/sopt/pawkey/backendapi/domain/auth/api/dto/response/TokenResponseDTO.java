package org.sopt.pawkey.backendapi.domain.auth.api.dto.response;

public record TokenResponseDTO(String accessToken, String refreshToken) {
}