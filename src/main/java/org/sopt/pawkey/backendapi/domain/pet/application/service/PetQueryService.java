package org.sopt.pawkey.backendapi.domain.pet.application.service;

import static org.sopt.pawkey.backendapi.domain.pet.api.dto.response.PetProfileResponseDto.*;

import java.util.List;

import org.sopt.pawkey.backendapi.domain.dbti.domain.repository.DbtiRepository;
import org.sopt.pawkey.backendapi.domain.pet.api.dto.response.PetProfileResponseDto;
import org.sopt.pawkey.backendapi.domain.pet.domain.repository.PetRepository;
import org.sopt.pawkey.backendapi.domain.pet.exception.PetBusinessException;
import org.sopt.pawkey.backendapi.domain.pet.exception.PetErrorCode;
import org.sopt.pawkey.backendapi.domain.pet.infra.persistence.entity.PetEntity;
import org.sopt.pawkey.backendapi.domain.pet.api.dto.response.BreedListResponseDto;
import org.sopt.pawkey.backendapi.domain.pet.domain.repository.BreedRepository;
import org.sopt.pawkey.backendapi.domain.user.exception.UserBusinessException;
import org.sopt.pawkey.backendapi.domain.user.exception.UserErrorCode;
import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.UserEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.sopt.pawkey.backendapi.domain.dbti.infra.persistence.entity.DbtiEntity;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PetQueryService {

	private final BreedRepository breedRepository;
	private final DbtiRepository dbtiRepository;

	private final PetRepository petRepository;

	@Transactional(readOnly = true)
	public PetProfileResponseDto getPetProfile(UserEntity user) {
		PetEntity pet = petRepository.findByUserId(user.getUserId())
			.orElseThrow(() -> new UserBusinessException(UserErrorCode.USER_PET_NOT_REGISTERED));

		String dbtiDescription = null;
		if (pet.getDbtiResult() != null) {
			dbtiDescription = dbtiRepository.findDbtiByType(pet.getDbtiResult().getDbtiType())
				.map(DbtiEntity::getName)
				.orElse(null);
		}

		String formattedAge = formatAge(pet.getBirth());
		return PetProfileResponseDto.of(pet, formattedAge, dbtiDescription);
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