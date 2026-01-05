package org.sopt.pawkey.backendapi.domain.pet.application.dto.request;

import java.time.LocalDate;

public record UpdatePetCommand(
	String name,
	LocalDate birth,
	String gender,
	boolean isNeutered,
	String breed
) {
}
