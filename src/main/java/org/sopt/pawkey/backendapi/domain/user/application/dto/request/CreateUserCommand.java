package org.sopt.pawkey.backendapi.domain.user.application.dto.request;

import java.time.LocalDate;

public record CreateUserCommand(
	String name,
	LocalDate birth,
	String gender,
	Long regionId
) {

	public static CreateUserCommand of(String name, LocalDate birth, String gender, Long regionId) {
		return new CreateUserCommand(name, birth, gender, regionId);
	}
}