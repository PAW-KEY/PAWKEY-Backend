package org.sopt.pawkey.backendapi.domain.pet.api.dto.response;

import java.util.List;

public record PetProfileResponseDto(
	Long petId,
	String name,
	String gender,
	boolean isNeutered,
	int age,
	boolean isAgeKnown,
	String breed,
	String imageUrl,
	List<TraitDto> traits,
	int walkCount
) {
	public record TraitDto(
		String category,
		String option
	) {
	}
}