package org.sopt.pawkey.backendapi.domain.user.application.service;

import java.util.List;

import org.sopt.pawkey.backendapi.domain.dbti.domain.repository.DbtiResultRepository;
import org.sopt.pawkey.backendapi.domain.pet.api.dto.response.PetProfileResponseDto;
import org.sopt.pawkey.backendapi.domain.pet.domain.repository.PetRepository;
import org.sopt.pawkey.backendapi.domain.pet.infra.persistence.entity.PetEntity;
import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.UserEntity;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserPetQueryService {

	private final PetRepository petRepository;
	private final DbtiResultRepository dbtiResultRepository;

	public List<PetProfileResponseDto> getPetProfiles(UserEntity user) {
		List<PetEntity> petEntityList = petRepository.findAllPetsByUserId(user.getUserId());

		return petEntityList.stream()
			.map(pet -> {
				String dbtiName = dbtiResultRepository.findByPetId(pet.getPetId())
					.map(result -> result.getDbtiType().name())
					.orElse(null);

				return PetProfileResponseDto.of(pet, dbtiName);
			})
			.toList();
	}
}