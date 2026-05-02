package org.sopt.pawkey.backendapi.domain.user.api.dto.request;

import java.time.LocalDate;

import org.sopt.pawkey.backendapi.domain.user.application.dto.request.UpdateUserInfoCommand;
import org.sopt.pawkey.backendapi.global.enums.Gender;

import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

public record UpdateUserInfoRequestDto(
	@Size(min = 1, message = "닉네임은 1자 이상이어야 합니다.")
	String name,

	@PastOrPresent(message = "생년월일은 현재 또는 과거 날짜여야 합니다")
	LocalDate birth,

	Gender gender
) {
	public UpdateUserInfoCommand toCommand() {
		return new UpdateUserInfoCommand(name, birth, gender);
	}
}