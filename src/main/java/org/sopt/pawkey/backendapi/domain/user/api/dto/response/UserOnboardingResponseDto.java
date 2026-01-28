package org.sopt.pawkey.backendapi.domain.user.api.dto.response;

import org.sopt.pawkey.backendapi.domain.pet.infra.persistence.entity.PetEntity;
import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.UserEntity;

public record UserOnboardingResponseDto(
	Long userId,
	Long petId
) {

	public static UserOnboardingResponseDto from(UserEntity user, PetEntity pet) {
		return new UserOnboardingResponseDto(
			user.getUserId(),
			pet.getPetId()
		);
	}
}
