package org.sopt.pawkey.backendapi.domain.pet.api.dto.request;

import java.util.List;

import org.sopt.pawkey.backendapi.domain.pet.application.dto.SelectedOptionForPetTraitCategory;
import org.sopt.pawkey.backendapi.domain.pet.application.dto.request.CreatePetCommand;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

public record PetRequestDto(
	@NotBlank(message = "반려동물 이름은 필수값입니다.") String name,
	@NotBlank(message = "성별은 필수값입니다.") @Pattern(regexp = "^(M|F)$", message = "성별은 M 또는 F이어야 합니다.") String gender,
	@Positive(message = "나이는 양수여야 합니다.") int age,
	boolean isAgeKnown,
	boolean isNeutered,
	@NotBlank(message = "품종은 필수값입니다.") String breed,
	@NotNull(message = "반려동물 성향은 필수값입니다.") List<SelectedOptionForPetTraitCategory> petTraits
) {
	public CreatePetCommand toCommand() {
		return CreatePetCommand.of(name, gender, age, isAgeKnown, isNeutered, breed, petTraits);
	}
}
