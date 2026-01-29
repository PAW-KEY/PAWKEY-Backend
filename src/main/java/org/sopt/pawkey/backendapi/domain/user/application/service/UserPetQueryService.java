package org.sopt.pawkey.backendapi.domain.user.application.service;

import org.sopt.pawkey.backendapi.domain.pet.api.dto.response.PetProfileResponseDto;
import org.sopt.pawkey.backendapi.domain.pet.domain.repository.PetRepository;
import org.sopt.pawkey.backendapi.domain.pet.infra.persistence.entity.PetEntity;
import org.sopt.pawkey.backendapi.domain.user.exception.UserBusinessException;
import org.sopt.pawkey.backendapi.domain.user.exception.UserErrorCode;
import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.UserEntity;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserPetQueryService {

	private final PetRepository petRepository;

	public PetProfileResponseDto getPetProfile(UserEntity user) {
		PetEntity pet = petRepository.findByUserId(user.getUserId())
			.orElseThrow(() -> new UserBusinessException(UserErrorCode.USER_PET_NOT_REGISTERED));

		return PetProfileResponseDto.from(pet);
	}
}