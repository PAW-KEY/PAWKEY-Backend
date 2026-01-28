package org.sopt.pawkey.backendapi.domain.user.application.dto.request;

import org.sopt.pawkey.backendapi.domain.pet.application.dto.request.CreatePetCommand;

public record UserOnboardingCommand(
	CreateUserCommand userCommand,
	CreatePetCommand petCommand
) {
}
