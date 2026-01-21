package org.sopt.pawkey.backendapi.domain.user.api.dto.response;

import java.time.LocalDate;

import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.UserEntity;

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

	private static String parseGender(String gender) {
		if (gender == null)
			return "미선택";
		return switch (gender.toUpperCase()) {
			case "M" -> "남성";
			case "F" -> "여성";
			default -> gender;
		};
	}
}