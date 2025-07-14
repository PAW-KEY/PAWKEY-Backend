package org.sopt.pawkey.backendapi.domain.user.api.dto.request;

import org.sopt.pawkey.backendapi.domain.pet.api.dto.request.PetRequestDto;
import org.sopt.pawkey.backendapi.domain.user.application.dto.request.CreateUserCommand;
import org.sopt.pawkey.backendapi.domain.user.application.dto.request.UserRegisterCommand;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateUserRequestDto(

	@NotNull(message = "로그인 아이디는 필수값입니다.") String loginId,
	@Size(min = 8, max = 50, message = "비밀번호는 8자 이상 50자 이하여야 합니다.") String password,

	@NotNull(message = "이름은 필수값입니다.") String name,
	@NotNull(message = "성별은 필수값입니다.")
	@Pattern(regexp = "^(M|F)$", message = "성별은 MALE 또는 FEMALE이어야 합니다.") String gender,
	@NotNull(message = "나이는 필수값입니다.") int age,
	@NotNull(message = "활동 지역은 필수값입니다.") Long regionId,
	@NotNull(message = "강아지 정보 입력은 필수값입니다.") PetRequestDto pet

) {

	public UserRegisterCommand toCommand() {
		return new UserRegisterCommand(
			CreateUserCommand.of(loginId, password, name, gender, age, regionId),
			pet.toCommand()
		);
	}
}
