package org.sopt.pawkey.backendapi.domain.pet.api.dto.request;

import java.time.LocalDate;

import org.sopt.pawkey.backendapi.domain.pet.application.dto.request.UpdatePetCommand;
import org.sopt.pawkey.backendapi.global.enums.Gender;

import jakarta.validation.constraints.PastOrPresent;

public record UpdatePetRequestDto(
	String name,

	@PastOrPresent(message = "생년월일은 현재 또는 과거 날짜여야 합니다")
	LocalDate birth,

	Gender gender,

	Boolean isNeutered,

	Long breedId,
	Long imageId
) {
	public UpdatePetCommand toCommand() {
		return UpdatePetCommand.of(name, gender, birth, isNeutered, breedId, imageId);
	}
}