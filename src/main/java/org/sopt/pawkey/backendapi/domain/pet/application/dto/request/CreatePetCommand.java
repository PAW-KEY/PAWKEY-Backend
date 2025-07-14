package org.sopt.pawkey.backendapi.domain.pet.application.dto.request;

import java.util.List;

import org.sopt.pawkey.backendapi.domain.pet.application.dto.SelectedOptionForPetTraitCategory;

public record CreatePetCommand(String name,
							   String gender,
							   int age,
							   boolean isAgeKnown,
							   boolean isNeutered,
							   String breed,
							   List<SelectedOptionForPetTraitCategory> petTraits) {
	public static CreatePetCommand of(String name, String gender, int age, boolean isAgeKnown, boolean isNeutered, String breed,List<SelectedOptionForPetTraitCategory> petTraits) {
		return new CreatePetCommand(name, gender, age, isAgeKnown, isNeutered, breed,petTraits);
	}
}
