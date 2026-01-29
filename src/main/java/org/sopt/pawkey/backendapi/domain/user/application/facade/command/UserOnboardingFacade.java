package org.sopt.pawkey.backendapi.domain.user.application.facade.command;

import org.sopt.pawkey.backendapi.domain.pet.application.service.PetService;
import org.sopt.pawkey.backendapi.domain.pet.infra.persistence.entity.PetEntity;
import org.sopt.pawkey.backendapi.domain.region.application.service.RegionService;
import org.sopt.pawkey.backendapi.domain.region.infra.persistence.entity.RegionEntity;
import org.sopt.pawkey.backendapi.domain.user.api.dto.response.UserOnboardingResponseDto;
import org.sopt.pawkey.backendapi.domain.user.application.dto.request.UserOnboardingCommand;
import org.sopt.pawkey.backendapi.domain.user.application.service.UserService;
import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.UserEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@Transactional
public class UserOnboardingFacade {

	private final UserService userService;
	private final PetService petService;
	private final RegionService regionService;

	public UserOnboardingResponseDto onboard(Long currentUserId, UserOnboardingCommand command) {
		RegionEntity region = regionService.getDongTypeRegionByIdOrThrow(command.userCommand().regionId());
		UserEntity user = userService.completeOnboarding(currentUserId, command.userCommand(), region);

		PetEntity pet = petService.savePet(command.petCommand(), user);

		return UserOnboardingResponseDto.from(user, pet);
	}
}
