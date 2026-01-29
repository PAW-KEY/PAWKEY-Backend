package org.sopt.pawkey.backendapi.domain.pet.application.dto.request;

import java.time.LocalDate;

import org.sopt.pawkey.backendapi.global.enums.Gender;

public record UpdatePetCommand(
	String name,
	LocalDate birth,
	Gender gender,
	boolean isNeutered,
	String breed
) {
}
