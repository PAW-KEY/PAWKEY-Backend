package org.sopt.pawkey.backendapi.domain.pet.application.service;

import org.sopt.pawkey.backendapi.domain.pet.application.dto.request.CreatePetCommand;
import org.sopt.pawkey.backendapi.domain.pet.domain.repository.BreedRepository;
import org.sopt.pawkey.backendapi.domain.pet.domain.repository.PetRepository;
import org.sopt.pawkey.backendapi.domain.pet.exception.PetBusinessException;
import org.sopt.pawkey.backendapi.domain.pet.exception.PetErrorCode;
import org.sopt.pawkey.backendapi.domain.pet.infra.persistence.entity.BreedEntity;
import org.sopt.pawkey.backendapi.domain.pet.infra.persistence.entity.PetEntity;
import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.UserEntity;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PetService {

	private final PetRepository petRepository;
	private final BreedRepository breedRepository;

	public PetEntity savePet(CreatePetCommand command, UserEntity user) {
		BreedEntity breed = breedRepository.findBreedById(command.breedId())
			.orElseThrow(() -> new PetBusinessException(PetErrorCode.BREED_NOT_FOUND));

		PetEntity pet = PetEntity.builder()
			.name(command.name())
			.gender(command.gender())
			.birth(command.birth())
			.isNeutered(command.isNeutered())
			.breed(breed)
			.user(user)
			.walkCount(0)
			.build();

		return petRepository.save(pet);
	}
}
