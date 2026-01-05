package org.sopt.pawkey.backendapi.domain.pet.application.service;

import org.sopt.pawkey.backendapi.domain.pet.application.dto.request.CreatePetCommand;
import org.sopt.pawkey.backendapi.domain.pet.application.dto.request.UpdatePetCommand;
import org.sopt.pawkey.backendapi.domain.pet.domain.repository.PetRepository;
import org.sopt.pawkey.backendapi.domain.pet.exception.PetBusinessException;
import org.sopt.pawkey.backendapi.domain.pet.exception.PetErrorCode;
import org.sopt.pawkey.backendapi.domain.pet.infra.persistence.entity.PetEntity;
import org.sopt.pawkey.backendapi.domain.user.exception.UserBusinessException;
import org.sopt.pawkey.backendapi.domain.user.exception.UserErrorCode;
import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.UserEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PetService {

	private final PetRepository petRepository;

	public PetEntity savePet(CreatePetCommand command, UserEntity user) {
		PetEntity pet = PetEntity.builder()
			.name(command.name())
			.gender(command.gender())
			.birth(command.birth())
			.isNeutered(command.isNeutered())
			.breed(command.breed())
			.user(user)
			.walkCount(0)
			.build();

		return petRepository.save(pet);
	}

	@Transactional
	public void updatePetInfo(Long userId, Long petId, UpdatePetCommand command) {
		PetEntity pet = petRepository.findById(petId)
			.orElseThrow(() -> new PetBusinessException(PetErrorCode.PET_NOT_FOUND));

		if (!pet.getUser().getUserId().equals(userId)) {
			throw new UserBusinessException(UserErrorCode.UNAUTHORIZED_ACCESS);
		}

		pet.updateProfile(
			command.name(),
			command.birth(),
			command.gender(),
			command.isNeutered(),
			command.breed()
		);
	}
}
