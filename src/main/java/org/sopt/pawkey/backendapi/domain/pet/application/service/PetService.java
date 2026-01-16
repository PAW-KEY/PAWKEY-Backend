package org.sopt.pawkey.backendapi.domain.pet.application.service;

import org.sopt.pawkey.backendapi.domain.pet.application.dto.request.CreatePetCommand;
import org.sopt.pawkey.backendapi.domain.pet.domain.repository.PetRepository;
import org.sopt.pawkey.backendapi.domain.pet.infra.persistence.entity.PetEntity;
import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.UserEntity;
import org.springframework.stereotype.Service;

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
}
