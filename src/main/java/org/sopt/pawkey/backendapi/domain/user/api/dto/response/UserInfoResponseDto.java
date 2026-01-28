package org.sopt.pawkey.backendapi.domain.user.api.dto.response;

import java.time.LocalDate;

import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.UserEntity;
import org.sopt.pawkey.backendapi.global.enums.Gender;

public record UserInfoResponseDto(
	String name,
	String email,
	LocalDate birth,
	String gender
) {
	public static UserInfoResponseDto of(UserEntity user, String email) {
		return new UserInfoResponseDto(
			user.getName(),
			email,
			user.getBirth(),
			parseGender(user.getGender())
		);
	}

	private static String parseGender(Gender gender) {
		if (gender == null)
			return "미선택";

		return switch (gender) {
			case M -> "남성";
			case F -> "여성";
		};
	}
}