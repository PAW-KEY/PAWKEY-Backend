package org.sopt.pawkey.backendapi.domain.pet.application.facade;

import org.sopt.pawkey.backendapi.domain.pet.application.dto.request.UpdatePetCommand;
import org.sopt.pawkey.backendapi.domain.pet.application.service.PetCommandService;
import org.sopt.pawkey.backendapi.domain.pet.application.service.PetQueryService;
import org.sopt.pawkey.backendapi.domain.pet.infra.persistence.entity.PetEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PetCommandFacade {

	private final PetCommandService petCommandService;
	private final PetQueryService petQueryService;

	@Transactional
	public void updatePetInfo(Long userId, Long petId, UpdatePetCommand command) {
		PetEntity pet = petQueryService.getPetOwnedByUser(userId, petId);
		petCommandService.updatePetInfo(pet, command);
	}
}