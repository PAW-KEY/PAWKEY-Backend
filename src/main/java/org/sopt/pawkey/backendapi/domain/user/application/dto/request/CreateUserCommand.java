package org.sopt.pawkey.backendapi.domain.user.application.dto.request;

import org.sopt.pawkey.backendapi.domain.pet.api.dto.request.PetRequestDto;
import org.sopt.pawkey.backendapi.domain.pet.application.dto.request.CreatePetCommand;

import jakarta.validation.constraints.NotNull;

public record CreateUserCommand(
	String loginId,
	String password,
	String name,
	String gender,
	int age,
	Long regionId
) {

	/**
	 * Creates a new {@code CreateUserCommand} instance with the specified user credentials and profile information.
	 *
	 * @param loginId   the user's login identifier
	 * @param password  the user's password
	 * @param name      the user's name
	 * @param gender    the user's gender
	 * @param age       the user's age
	 * @param regionId  the identifier of the user's region
	 * @return a new {@code CreateUserCommand} containing the provided user details
	 */
	public static CreateUserCommand of(String loginId, String password, String name, String gender, int age, Long regionId) {
		return new CreateUserCommand(loginId, password, name, gender, age, regionId);
	}

}
