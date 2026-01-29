package org.sopt.pawkey.backendapi.domain.pet.application.facade;

import org.sopt.pawkey.backendapi.domain.pet.api.dto.response.PetProfileResponseDto;
import org.sopt.pawkey.backendapi.domain.pet.application.dto.request.UpdatePetCommand;
import org.sopt.pawkey.backendapi.domain.pet.application.service.PetQueryService;
import org.sopt.pawkey.backendapi.domain.pet.application.service.PetService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PetCommandFacade {

	private final PetService petService;

	@Transactional
	public void updatePetInfo(Long userId, Long petId, UpdatePetCommand command) {
		petService.updatePetInfo(userId, petId, command);
	}
}