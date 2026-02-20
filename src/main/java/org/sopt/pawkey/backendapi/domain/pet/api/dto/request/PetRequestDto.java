package org.sopt.pawkey.backendapi.domain.pet.api.dto.request;

import java.time.LocalDate;

import org.sopt.pawkey.backendapi.domain.pet.application.dto.request.CreatePetCommand;
import org.sopt.pawkey.backendapi.global.enums.Gender;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PetRequestDto(
	@NotBlank(message = "반려동물 이름은 필수값입니다.") String name,
	@NotNull(message = "성별은 필수값입니다.") Gender gender,
	@NotNull(message = "생년월일은 필수값입니다.")
	@JsonFormat(pattern = "yyyy-MM-dd") LocalDate birth,
	@NotNull Boolean isNeutered,
	@NotNull(message = "견종은 필수값입니다.") Long breedId,
	Long imageId
) {
	public CreatePetCommand toCommand() {
		return CreatePetCommand.of(name, gender, birth, isNeutered, breedId, imageId);
	}
}
