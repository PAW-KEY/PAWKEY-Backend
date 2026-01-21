package org.sopt.pawkey.backendapi.domain.pet.application.service;

import java.util.List;

import org.sopt.pawkey.backendapi.domain.pet.api.dto.response.BreedListResponseDto;
import org.sopt.pawkey.backendapi.domain.pet.domain.repository.BreedRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PetQueryService {

	private final BreedRepository breedRepository;

	public List<BreedListResponseDto.BreedDto> getAllBreeds() {
		return breedRepository.findAll().stream()
			.map(breed -> new BreedListResponseDto.BreedDto(
				breed.getId(),
				breed.getName()
			))
			.toList();
	}
}
