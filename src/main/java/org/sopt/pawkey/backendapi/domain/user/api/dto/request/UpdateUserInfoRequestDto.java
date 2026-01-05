package org.sopt.pawkey.backendapi.domain.user.api.dto.request;

import java.time.LocalDate;

import org.sopt.pawkey.backendapi.domain.user.application.dto.request.UpdateUserInfoCommand;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateUserInfoRequestDto(
	@NotBlank(message = "닉네임은 필수 입력 사항입니다.")
	String name,

	@NotNull(message = "생년월일은 필수 입력 사항입니다.")
	LocalDate birth,

	@NotBlank(message = "성별 선택은 필수 사항입니다.")
	String gender
) {
	public UpdateUserInfoCommand toCommand() {
		return new UpdateUserInfoCommand(name, birth, gender);
	}
}