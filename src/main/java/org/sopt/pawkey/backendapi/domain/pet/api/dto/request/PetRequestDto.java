package org.sopt.pawkey.backendapi.domain.pet.api.dto.request;

import java.util.List;

import org.sopt.pawkey.backendapi.domain.pet.application.dto.SelectedOptionForPetTraitCategory;
import org.sopt.pawkey.backendapi.domain.pet.application.dto.request.CreatePetCommand;

public record PetRequestDto(
	String name,
	String gender,
	int age,
	boolean isAgeKnown,
	boolean isNeutered,
	String breed,
	List<SelectedOptionForPetTraitCategory> petTraits
) {
	/**
	 * Converts this PetRequestDto into a CreatePetCommand containing all pet details.
	 *
	 * @return a CreatePetCommand initialized with the data from this DTO
	 */
	public CreatePetCommand toCommand() {
		return CreatePetCommand.of(name, gender, age, isAgeKnown, isNeutered, breed,petTraits);
	}
}
