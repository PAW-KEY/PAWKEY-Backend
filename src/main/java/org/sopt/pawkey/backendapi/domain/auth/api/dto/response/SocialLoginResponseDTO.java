package org.sopt.pawkey.backendapi.domain.auth.api.dto.response;

import lombok.Builder;

@Builder
public record SocialLoginResponseDTO(
	String accessToken,
	String refreshToken,
	boolean isNewUser,
	Long userId,
	Long petId
) {
	public static SocialLoginResponseDTO of(TokenResponseDTO tokenResponse, boolean isNewUser,  Long userId, Long petId){
		return SocialLoginResponseDTO.builder()
			.accessToken(tokenResponse.accessToken())
			.refreshToken(tokenResponse.refreshToken())
			.isNewUser(isNewUser)
			.userId(userId)
			.petId(petId)
			.build();
	}
}
