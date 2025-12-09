package org.sopt.pawkey.backendapi.global.auth.api.dto.response;

import org.h2.command.Token;

import lombok.Builder;

@Builder
public record SocialLoginResponseDTO(
	String accessToken,
	String refreshToken,
	boolean isNewUser
) {
	public static SocialLoginResponseDTO of(TokenResponseDTO tokenResponse, boolean isNewUser){
		return SocialLoginResponseDTO.builder()
			.accessToken(tokenResponse.accessToken())
			.refreshToken(tokenResponse.refreshToken())
			.isNewUser(isNewUser)
			.build();
	}
}
