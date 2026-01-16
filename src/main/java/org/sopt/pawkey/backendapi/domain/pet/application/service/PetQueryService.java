package org.sopt.pawkey.backendapi.domain.pet.application.service;

<<<<<<< HEAD
=======
import java.util.List;

import org.sopt.pawkey.backendapi.domain.pet.api.dto.response.BreedListResponseDto;
import org.sopt.pawkey.backendapi.domain.pet.application.dto.response.PetTraitCategoryResult;
import org.sopt.pawkey.backendapi.domain.pet.domain.repository.BreedRepository;
>>>>>>> origin/feat/#206
import org.sopt.pawkey.backendapi.domain.pet.domain.repository.PetRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PetQueryService {

	private final PetRepository petRepository;
	private final BreedRepository breedRepository;

<<<<<<< HEAD
=======
	public List<PetTraitCategoryResult> getAllPetTraitCategories() {
		List<PetTraitCategoryEntity> petTraitCategoryEntityList = petRepository.findAllPetTraitCategoriesWithOptions();

		return petTraitCategoryEntityList.stream()
			.map(PetTraitCategoryResult::fromEntity) // static fromDomain() 메서드 있다고 가정
			.toList();
	}

	public List<BreedListResponseDto.BreedDto> getAllBreeds() {
		return breedRepository.findAll().stream()
			.map(breed -> new BreedListResponseDto.BreedDto(breed.getId(), breed.getName()))
			.toList();
	}
>>>>>>> origin/feat/#206
}
