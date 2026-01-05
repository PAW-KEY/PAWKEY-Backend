package org.sopt.pawkey.backendapi.domain.pet.api.dto.request;

import java.time.LocalDate;

import org.sopt.pawkey.backendapi.domain.pet.application.dto.request.UpdatePetCommand;
import org.sopt.pawkey.backendapi.domain.user.application.dto.request.UpdateUserInfoCommand;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdatePetRequestDto(
	@NotBlank String name,

	@NotNull LocalDate birth,

	@Schema(description = "반려견 성별 (M/F)", example = "M")
	@NotBlank String gender,

	@NotNull Boolean isNeutered,

	@NotBlank String breed
) {
	public UpdatePetCommand toCommand() {
		return new UpdatePetCommand(name, birth, gender, isNeutered, breed);
	}
}