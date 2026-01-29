package org.sopt.pawkey.backendapi.domain.pet.api.dto.request;

import java.time.LocalDate;

import org.sopt.pawkey.backendapi.domain.pet.application.dto.request.CreatePetCommand;
import org.sopt.pawkey.backendapi.global.enums.Gender;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record PetRequestDto(
	@NotBlank(message = "반려동물 이름은 필수값입니다.") String name,
	@NotNull(message = "성별은 필수값입니다.") Gender gender,
	@NotNull(message = "생년월일은 필수값입니다.")
	@JsonFormat(pattern = "yyyy-MM-dd") LocalDate birth,
	boolean isNeutered,
	@NotNull(message = "품종은 필수값입니다.") Long breedId
) {
	public CreatePetCommand toCommand() {
		return CreatePetCommand.of(name, gender, birth, isNeutered, breedId);
	}
}
