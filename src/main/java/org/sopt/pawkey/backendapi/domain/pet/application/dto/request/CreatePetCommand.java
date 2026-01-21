package org.sopt.pawkey.backendapi.domain.pet.application.dto.request;

import java.time.LocalDate;

public record CreatePetCommand(
	String name,
	String gender,
	LocalDate birth,
	boolean isNeutered,
	Long breedId
) {
	public static CreatePetCommand of(
		String name,
		String gender,
		LocalDate birth,
		boolean isNeutered,
		Long breedId
	) {
		return new CreatePetCommand(name, gender, birth, isNeutered, breedId);
	}
}
