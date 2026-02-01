package org.sopt.pawkey.backendapi.domain.pet.application.facade;

import java.util.List;

import org.sopt.pawkey.backendapi.domain.pet.api.dto.response.BreedListResponseDto;
import org.sopt.pawkey.backendapi.domain.pet.api.dto.response.PetProfileResponseDto;
import org.sopt.pawkey.backendapi.domain.pet.application.service.PetQueryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PetQueryFacade {

	private final PetQueryService petQueryService;

	public BreedListResponseDto getBreedList() {
		List<BreedListResponseDto.BreedDto> breeds = petQueryService.getAllBreeds();
		return BreedListResponseDto.from(breeds);
	}

	public PetProfileResponseDto getPetProfile(Long userId, Long petId) {
		return petQueryService.getPetProfileDetail(userId, petId);
	}
}
