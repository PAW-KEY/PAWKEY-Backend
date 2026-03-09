package org.sopt.pawkey.backendapi.domain.pet.application.service;

import org.sopt.pawkey.backendapi.domain.image.domain.model.ImageType;
import org.sopt.pawkey.backendapi.domain.image.domain.repository.ImageRepository;
import org.sopt.pawkey.backendapi.domain.image.exception.ImageBusinessException;
import org.sopt.pawkey.backendapi.domain.image.exception.ImageErrorCode;
import org.sopt.pawkey.backendapi.domain.image.infra.persistence.entity.ImageEntity;
import org.sopt.pawkey.backendapi.domain.pet.application.dto.request.CreatePetCommand;
import org.sopt.pawkey.backendapi.domain.pet.application.dto.request.UpdatePetCommand;
import org.sopt.pawkey.backendapi.domain.pet.domain.repository.BreedRepository;
import org.sopt.pawkey.backendapi.domain.pet.domain.repository.PetRepository;
import org.sopt.pawkey.backendapi.domain.pet.exception.PetBusinessException;
import org.sopt.pawkey.backendapi.domain.pet.exception.PetErrorCode;
import org.sopt.pawkey.backendapi.domain.pet.infra.persistence.entity.BreedEntity;
import org.sopt.pawkey.backendapi.domain.pet.infra.persistence.entity.PetEntity;
import org.sopt.pawkey.backendapi.domain.user.exception.UserBusinessException;
import org.sopt.pawkey.backendapi.domain.user.exception.UserErrorCode;
import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.UserEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PetCommandService {

	private final PetRepository petRepository;
	private final BreedRepository breedRepository;
	private final ImageRepository imageRepository;

	public PetEntity savePet(CreatePetCommand command, UserEntity user) {

		if (petRepository.existsByUserId(user.getUserId())) {
			throw new PetBusinessException(PetErrorCode.ALREADY_REGISTERED_PET);
		}

		BreedEntity breed = breedRepository.findBreedById(command.breedId())
			.orElseThrow(() -> new PetBusinessException(PetErrorCode.BREED_NOT_FOUND));

		ImageEntity profileImage = null;
		if (command.imageId() != null) {
			profileImage = imageRepository.findById(command.imageId())
				.orElseThrow(() -> new ImageBusinessException(ImageErrorCode.IMAGE_NOT_FOUND));
		}

		PetEntity pet = PetEntity.builder()
			.name(command.name())
			.gender(command.gender())
			.birth(command.birth())
			.isNeutered(command.isNeutered())
			.breed(breed)
			.user(user)
			.profileImage(profileImage)
			.build();

		return petRepository.save(pet);
	}

	@Transactional
	public void updatePetInfo(PetEntity pet, UpdatePetCommand command) {
		BreedEntity breed = (command.breedId() != null)
			? breedRepository.findBreedById(command.breedId())
			.orElseThrow(() -> new PetBusinessException(PetErrorCode.BREED_NOT_FOUND))
			: pet.getBreed();

		ImageEntity profileImage = null;
		if (command.imageId() != null) {
			profileImage = imageRepository.findById(command.imageId())
				.orElseThrow(() -> new ImageBusinessException(ImageErrorCode.IMAGE_NOT_FOUND));
		}
		
		pet.updateProfile(
			command.name() != null ? command.name() : pet.getName(),
			command.birth() != null ? command.birth() : pet.getBirth(),
			command.gender() != null ? command.gender() : pet.getGender(),
			command.isNeutered() != null ? command.isNeutered() : pet.isNeutered(),
			breed,
			profileImage
		);
	}
}