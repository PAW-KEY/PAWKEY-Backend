package org.sopt.pawkey.backendapi.domain.pet.application.dto.request;

import java.time.LocalDate;

public record CreatePetCommand(
	String name,
	String gender,
	LocalDate birth,
	boolean isNeutered,
	String breed
) {
	public static CreatePetCommand of(
		String name,
		String gender,
		LocalDate birth,
		boolean isNeutered,
		String breed
	) {
		return new CreatePetCommand(name, gender, birth, isNeutered, breed);
	}
}
