package org.sopt.pawkey.backendapi.domain.pet.application.dto.request;

import java.time.LocalDate;

import org.sopt.pawkey.backendapi.global.enums.Gender;

public record UpdatePetCommand(
	String name,
	Gender gender,
	LocalDate birth,
	boolean isNeutered,
	Long breedId,
	Long imageId
) {
	public static UpdatePetCommand of(
		String name,
		Gender gender,
		LocalDate birth,
		boolean isNeutered,
		Long breedId,
		Long imageId
	) {
		return new UpdatePetCommand(name, gender, birth, isNeutered, breedId, imageId);
	}
}
