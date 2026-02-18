package org.sopt.pawkey.backendapi.domain.pet.application.dto.request;

import java.time.LocalDate;

import org.sopt.pawkey.backendapi.global.enums.Gender;

public record CreatePetCommand(
	String name,
	Gender gender,
	LocalDate birth,
	boolean isNeutered,
	Long breedId,
	Long imageId
) {
	public static CreatePetCommand of(
		String name,
		Gender gender,
		LocalDate birth,
		boolean isNeutered,
		Long breedId,
		Long imageId
	) {
		return new CreatePetCommand(name, gender, birth, isNeutered, breedId, imageId);
	}
}
