package org.sopt.pawkey.backendapi.domain.pet.application.service;

import static org.sopt.pawkey.backendapi.domain.pet.api.dto.response.PetProfileResponseDto.*;

import java.time.LocalDate;
import java.util.List;

import org.sopt.pawkey.backendapi.domain.pet.domain.repository.PetRepository;
import org.sopt.pawkey.backendapi.domain.pet.exception.PetBusinessException;
import org.sopt.pawkey.backendapi.domain.pet.exception.PetErrorCode;
import org.sopt.pawkey.backendapi.domain.pet.infra.persistence.entity.PetEntity;
import org.sopt.pawkey.backendapi.domain.pet.api.dto.response.BreedListResponseDto;
import org.sopt.pawkey.backendapi.domain.pet.domain.repository.BreedRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PetQueryService {

	private final BreedRepository breedRepository;

	private final PetRepository petRepository;

	@Transactional(readOnly = true)
	public PetEntity getPetDetail(Long userId, Long petId) {
		return getPetOwnedByUser(userId, petId);
	}

	public String getFormattedAge(LocalDate birth) {
		return formatAge(birth);
	}

	public List<BreedListResponseDto.BreedDto> getAllBreeds() {
		return breedRepository.findAll().stream()
			.map(breed -> new BreedListResponseDto.BreedDto(
				breed.getId(),
				breed.getName()
			))
			.toList();
	}

	public PetEntity getPetOwnedByUser(Long userId, Long petId) {
		PetEntity pet = petRepository.findById(petId)
			.orElseThrow(() -> new PetBusinessException(PetErrorCode.PET_NOT_FOUND));

		if (pet.getUser() == null || !pet.getUser().getUserId().equals(userId)) {
			throw new PetBusinessException(PetErrorCode.PET_ACCESS_DENIED);
		}
		return pet;
	}
}