package org.sopt.pawkey.backendapi.domain.user.api.dto.request;

import java.time.LocalDate;

import org.sopt.pawkey.backendapi.domain.user.application.dto.request.UpdateUserInfoCommand;
import org.sopt.pawkey.backendapi.global.enums.Gender;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;

public record UpdateUserInfoRequestDto(
	@NotBlank(message = "닉네임은 필수 입력 사항입니다.")
	String name,

	@NotNull(message = "생년월일은 필수 입력 사항입니다.")
	@PastOrPresent(message = "생년월일은 현재 또는 과거 날짜여야 합니다")
	LocalDate birth,

	@NotNull(message = "성별 선택은 필수 사항입니다.")
	Gender gender
) {
	public UpdateUserInfoCommand toCommand() {
		return new UpdateUserInfoCommand(name, birth, gender);
	}
}