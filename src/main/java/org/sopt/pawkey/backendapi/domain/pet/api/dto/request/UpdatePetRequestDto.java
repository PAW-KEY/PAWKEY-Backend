package org.sopt.pawkey.backendapi.domain.pet.api.dto.request;

import java.time.LocalDate;

import org.sopt.pawkey.backendapi.domain.pet.application.dto.request.UpdatePetCommand;
import org.sopt.pawkey.backendapi.global.enums.Gender;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

public record UpdatePetRequestDto(
	@NotBlank String name,

	@PastOrPresent(message = "생년월일은 현재 또는 과거 날짜여야 합니다")
	@NotNull LocalDate birth,

	@NotNull(message = "성별은 필수입니다") Gender gender,

	@NotNull Boolean isNeutered,

	@NotNull(message = "견종은 필수값입니다.") Long breedId
) {
	public UpdatePetCommand toCommand() {
		return UpdatePetCommand.of(name, gender, birth, isNeutered, breedId);
	}
}