package org.sopt.pawkey.backendapi.domain.pet.application.facade;

import java.util.List;

import org.sopt.pawkey.backendapi.domain.pet.api.dto.response.BreedListResponseDto;
import org.sopt.pawkey.backendapi.domain.pet.api.dto.response.PetProfileResponseDto;
import org.sopt.pawkey.backendapi.domain.pet.application.service.PetQueryService;
import org.sopt.pawkey.backendapi.domain.user.application.service.UserService;
import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.UserEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PetQueryFacade {

	private final PetQueryService petQueryService;
	private final UserService userService;

	public BreedListResponseDto getBreedList() {
		List<BreedListResponseDto.BreedDto> breeds = petQueryService.getAllBreeds();
		return BreedListResponseDto.from(breeds);
	}

	public PetProfileResponseDto getUserPet(Long userId) {
		UserEntity user = userService.findById(userId);
		return petQueryService.getPetProfile(user);
	}
}
