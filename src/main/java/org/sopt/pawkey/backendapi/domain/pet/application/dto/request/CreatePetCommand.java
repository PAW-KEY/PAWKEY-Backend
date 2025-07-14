package org.sopt.pawkey.backendapi.domain.pet.application.dto.request;

public record CreatePetCommand(String name,
							   String gender,
							   int age,
							   boolean isAgeKnown,
							   boolean isNeutered,
							   String breed) {
	public static CreatePetCommand of(String name, String gender, int age, boolean isAgeKnown, boolean isNeutered, String breed) {
		return new CreatePetCommand(name, gender, age, isAgeKnown, isNeutered, breed);
	}
}
