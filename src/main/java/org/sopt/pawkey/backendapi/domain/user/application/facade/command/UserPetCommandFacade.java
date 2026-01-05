package org.sopt.pawkey.backendapi.domain.user.application.facade.command;

import org.sopt.pawkey.backendapi.domain.pet.api.dto.response.PetProfileResponseDto;
import org.sopt.pawkey.backendapi.domain.pet.application.dto.request.UpdatePetCommand;
import org.sopt.pawkey.backendapi.domain.pet.application.service.PetService;
import org.sopt.pawkey.backendapi.domain.user.application.service.UserPetQueryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserPetCommandFacade {

	private final PetService petService;
	private final UserPetQueryService userPetQueryService;

	@Transactional
	public PetProfileResponseDto updatePetInfo(Long userId, Long petId, UpdatePetCommand command) {
		petService.updatePetInfo(userId, petId, command);
		return userPetQueryService.getPetProfile(petId);
	}
}