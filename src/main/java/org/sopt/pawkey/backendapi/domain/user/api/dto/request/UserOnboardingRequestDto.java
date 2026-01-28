package org.sopt.pawkey.backendapi.domain.user.api.dto.request;

import java.time.LocalDate;

import org.sopt.pawkey.backendapi.domain.pet.api.dto.request.PetRequestDto;
import org.sopt.pawkey.backendapi.domain.user.application.dto.request.OnboardingInfoCommand;
import org.sopt.pawkey.backendapi.domain.user.application.dto.request.UserOnboardingCommand;
import org.sopt.pawkey.backendapi.global.enums.Gender;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record UserOnboardingRequestDto(
	@NotNull(message = "이름은 필수값입니다.") String name,

	@NotNull(message = "생년월일은 필수값입니다.")
	@JsonFormat(pattern = "yyyy-MM-dd") LocalDate birth,

	@NotNull(message = "성별은 필수값입니다.") Gender gender,

	@NotNull(message = "활동 지역은 필수값입니다.") Long regionId,

	@Valid @NotNull(message = "강아지 정보 입력은 필수값입니다.") PetRequestDto pet
) {
	public UserOnboardingCommand toCommand() {

		return new UserOnboardingCommand(
			OnboardingInfoCommand.of(name, birth, gender, regionId),
			pet.toCommand()
		);
	}
}
