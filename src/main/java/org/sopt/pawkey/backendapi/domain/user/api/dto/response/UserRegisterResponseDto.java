package org.sopt.pawkey.backendapi.domain.user.api.dto.response;

import org.sopt.pawkey.backendapi.domain.pet.infra.persistence.entity.PetEntity;
import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.UserEntity;

public record UserRegisterResponseDto(
	Long userId,
	String userName,
	String loginId,
	Long petId,
	String petName
) {

	/**
	 * Creates a UserRegisterResponseDto from the provided user and pet entities.
	 *
	 * @param user the user entity containing user information
	 * @param pet the pet entity containing pet information
	 * @return a UserRegisterResponseDto populated with data from the given user and pet
	 */
	public static UserRegisterResponseDto from(UserEntity user, PetEntity pet) {
		return new UserRegisterResponseDto(
			user.getUserId(),
			user.getName(),
			user.getLoginId(),
			pet.getPetId(),
			pet.getName()
		);
	}
}
