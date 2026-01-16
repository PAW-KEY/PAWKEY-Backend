package org.sopt.pawkey.backendapi.domain.user.api.dto.response;

import java.time.LocalDate;

import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.UserEntity;

public record UserInfoResponseDto(
	String name,
	String gender,
	LocalDate birth,
	String activeRegion
) {
	public static UserInfoResponseDto from(UserEntity user) {
		return new UserInfoResponseDto(
			user.getName(),
			user.getGender(),
			user.getBirth(),
			user.getRegion().getFullRegionName()

		);
	}
}