package org.sopt.pawkey.backendapi.domain.pet.application.facade;

import java.util.List;

import org.sopt.pawkey.backendapi.domain.pet.api.dto.response.BreedListResponseDto;
import org.sopt.pawkey.backendapi.domain.pet.application.service.PetQueryService;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PetFacade {

	private final PetQueryService petQueryService;

	public BreedListResponseDto getBreedList() {
		List<BreedListResponseDto.BreedDto> breeds = petQueryService.getAllBreeds();
		return BreedListResponseDto.from(breeds);
	}
}
