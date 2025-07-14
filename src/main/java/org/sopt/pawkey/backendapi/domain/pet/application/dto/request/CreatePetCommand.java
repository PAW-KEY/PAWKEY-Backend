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
	/**
	 * Creates a new {@code CreatePetCommand} instance with the specified pet details.
	 *
	 * @param name the name of the pet
	 * @param gender the gender of the pet
	 * @param age the age of the pet
	 * @param isAgeKnown whether the pet's age is known
	 * @param isNeutered whether the pet is neutered
	 * @param breed the breed of the pet
	 * @param petTraits the list of selected trait options for the pet
	 * @return a new {@code CreatePetCommand} containing the provided information
	 */
	public static CreatePetCommand of(String name, String gender, int age, boolean isAgeKnown, boolean isNeutered, String breed,List<SelectedOptionForPetTraitCategory> petTraits) {
		return new CreatePetCommand(name, gender, age, isAgeKnown, isNeutered, breed,petTraits);
	}
}
