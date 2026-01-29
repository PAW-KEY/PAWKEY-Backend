package org.sopt.pawkey.backendapi.domain.pet.application.service;

import java.util.List;

import org.sopt.pawkey.backendapi.domain.pet.api.dto.response.PetProfileResponseDto;
import org.sopt.pawkey.backendapi.domain.pet.domain.repository.PetRepository;
import org.sopt.pawkey.backendapi.domain.pet.exception.PetBusinessException;
import org.sopt.pawkey.backendapi.domain.pet.exception.PetErrorCode;
import org.sopt.pawkey.backendapi.domain.pet.infra.persistence.entity.PetEntity;
import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.UserEntity;
import org.sopt.pawkey.backendapi.domain.pet.api.dto.response.BreedListResponseDto;
import org.sopt.pawkey.backendapi.domain.pet.domain.repository.BreedRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PetQueryService {

	private final BreedRepository breedRepository;
  
  private final PetRepository petRepository;

	public List<PetProfileResponseDto> getPetProfiles(UserEntity user) {
		List<PetEntity> petEntityList = petRepository.findAllPetsByUserId(user.getUserId());

		return petEntityList.stream()
			.map(PetProfileResponseDto::from)
			.toList();
	}

	public PetProfileResponseDto getPetProfile(Long petId) {
		PetEntity pet = petRepository.findById(petId)
			.orElseThrow(() -> new PetBusinessException(PetErrorCode.PET_NOT_FOUND));

		return PetProfileResponseDto.from(pet);
	}

	public List<BreedListResponseDto.BreedDto> getAllBreeds() {
		return breedRepository.findAll().stream()
			.map(breed -> new BreedListResponseDto.BreedDto(
				breed.getId(),
				breed.getName()
			))
			.toList();
	}
}
