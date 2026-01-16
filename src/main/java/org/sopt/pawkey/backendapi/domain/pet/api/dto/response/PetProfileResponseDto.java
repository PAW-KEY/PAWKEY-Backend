package org.sopt.pawkey.backendapi.domain.pet.api.dto.response;

import java.time.LocalDate;

public record PetProfileResponseDto(
	Long petId,
	String name,
	String gender,
	boolean isNeutered,
	LocalDate birth,
	String breed,
	String imageUrl,
	int walkCount
) {

}