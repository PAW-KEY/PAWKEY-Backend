package org.sopt.pawkey.backendapi.domain.pet.api.dto.request;

import java.time.LocalDate;

import org.sopt.pawkey.backendapi.domain.pet.application.dto.request.UpdatePetCommand;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;

public record UpdatePetRequestDto(
	@NotBlank String name,

	@PastOrPresent(message = "생년월일은 현재 또는 과거 날짜여야 합니다")
	@NotNull LocalDate birth,

	@Pattern(regexp = "^(M|F)$", message = "성별은 M 또는 F이어야 합니다.")
	@NotBlank String gender,

	@NotNull Boolean isNeutered,

	@NotBlank String breed
) {
	public UpdatePetCommand toCommand() {
		return new UpdatePetCommand(name, birth, gender, isNeutered, breed);
	}
}