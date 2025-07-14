package org.sopt.pawkey.backendapi.domain.pet.api.dto.request;

import org.sopt.pawkey.backendapi.domain.pet.application.dto.request.CreatePetCommand;

public record PetRequestDto(
	String name,
	String gender,
	int age,
	boolean isAgeKnown,
	boolean isNeutered,
	String breed
) {
	public CreatePetCommand toCommand() {
		return CreatePetCommand.of(name, gender, age, isAgeKnown, isNeutered, breed);
	}
}
