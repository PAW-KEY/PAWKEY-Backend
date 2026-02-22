package org.sopt.pawkey.backendapi.domain.user.api.dto;

import org.sopt.pawkey.backendapi.domain.pet.infra.persistence.entity.PetEntity;
import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.UserEntity;


public record AuthorDto(
	Long authorId,
	Long petId,
	String petName,
	String petProfileImage
) {
	public static AuthorDto from(UserEntity user, PetEntity pet) {
		return new AuthorDto(
				user.getUserId(),
				pet != null ? pet.getPetId() : null,
				pet != null ? pet.getName() : null,
				(pet != null && pet.getProfileImage() != null) ? pet.getProfileImage().getImageUrl() : null
		);
	}
}