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

	/**
	 * Creates and persists a new pet along with its selected traits.
	 *
	 * Constructs a new {@link PetEntity} using the provided command data, user, and profile image, then saves it to the repository.
	 * For each trait specified in the command, associates the pet with the corresponding trait option, throwing a {@link PetBusinessException}
	 * if any trait option is not found. Each selected trait is saved to the repository. Returns the saved pet entity.
	 *
	 * @param command      the command containing pet details and selected traits
	 * @param user         the user to whom the pet belongs
	 * @param profileImage the profile image for the pet
	 * @return the persisted {@link PetEntity} with associated traits
	 * @throws PetBusinessException if a specified trait option does not exist
	 */
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
