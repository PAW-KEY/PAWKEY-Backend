package org.sopt.pawkey.backendapi.domain.user.application.dto.request;

public record CreateUserCommand(
	String loginId,
	String password,
	String name,
	String gender,
	int age,
	Long regionId
) {

	public static CreateUserCommand of(String loginId, String password, String name, String gender, int age,
		Long regionId) {
		return new CreateUserCommand(loginId, password, name, gender, age, regionId);
	}
}
