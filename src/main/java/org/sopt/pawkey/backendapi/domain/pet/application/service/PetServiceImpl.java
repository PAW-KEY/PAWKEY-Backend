package org.sopt.pawkey.backendapi.domain.pet.application.service;

import java.util.List;

import org.sopt.pawkey.backendapi.domain.image.infra.persistence.entity.ImageEntity;
import org.sopt.pawkey.backendapi.domain.pet.application.dto.request.CreatePetCommand;
import org.sopt.pawkey.backendapi.domain.pet.domain.repository.PetRepository;
import org.sopt.pawkey.backendapi.domain.pet.domain.repository.PetTraitOptionRepository;
import org.sopt.pawkey.backendapi.domain.pet.domain.repository.PetTraitSelectedRepository;
import org.sopt.pawkey.backendapi.domain.pet.exception.PetBusinessException;
import org.sopt.pawkey.backendapi.domain.pet.exception.PetErrorCode;
import org.sopt.pawkey.backendapi.domain.pet.infra.persistence.entity.PetEntity;
import org.sopt.pawkey.backendapi.domain.pet.infra.persistence.entity.PetTraitOptionEntity;
import org.sopt.pawkey.backendapi.domain.pet.infra.persistence.entity.PetTraitSelectedEntity;
import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.UserEntity;
import org.sopt.pawkey.backendapi.global.exception.BusinessException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PetServiceImpl implements PetService {


	private final PetRepository petRepository;
	private final PetTraitOptionRepository petTraitOptionRepository;
	private final PetTraitSelectedRepository petTraitSelectedRepository;

	@Override
	public PetEntity savePet(CreatePetCommand command, UserEntity user, ImageEntity profileImage) {
		PetEntity pet = PetEntity.builder()
			.name(command.name())
			.gender(command.gender())
			.age(command.age())
			.isAgeKnown(command.isAgeKnown())
			.isNeutered(command.isNeutered())
			.breed(command.breed())
			.profileImage(profileImage)
			.user(user)
			.build();

		PetEntity savedPet = petRepository.save(pet);

		//성향 저장

		List<PetTraitSelectedEntity> selectedTraits = command.petTraits().stream()
			.map(dto -> {
				PetTraitOptionEntity option = petTraitOptionRepository.findById(dto.traitOptionId())
					.orElseThrow(()-> new PetBusinessException(PetErrorCode.CATEGORY_OPTION_NOT_FOUND));


				return PetTraitSelectedEntity.builder()
					.pet(savedPet)
					.petTraitOption(option)
					.build();
			})
			.toList();

		selectedTraits.forEach(petTraitSelectedRepository::save);

		return savedPet;
	}
}
