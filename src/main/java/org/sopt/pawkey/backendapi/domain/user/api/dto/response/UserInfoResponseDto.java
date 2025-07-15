package org.sopt.pawkey.backendapi.domain.user.api.dto.response;

import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.UserEntity;

public record UserInfoResponseDto(
	String name,
	String gender,
	int age,
	String activeRegion
) {
	public static UserInfoResponseDto from(UserEntity user) {
		return new UserInfoResponseDto(
			user.getName(),
			user.getGender(),
			user.getAge(),
			user.getRegion().getFullRegionName()

		);
	}
}