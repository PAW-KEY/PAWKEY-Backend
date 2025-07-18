package org.sopt.pawkey.backendapi.domain.user.application.service;

import java.util.List;

import org.sopt.pawkey.backendapi.domain.pet.api.dto.response.PetProfileResponseDto;
import org.sopt.pawkey.backendapi.domain.pet.domain.repository.PetRepository;
import org.sopt.pawkey.backendapi.domain.pet.infra.persistence.entity.PetEntity;
import org.sopt.pawkey.backendapi.domain.pet.infra.persistence.entity.PetTraitSelectedEntity;
import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.UserEntity;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserPetQueryService {

	private final PetRepository petRepository;
	
	public List<PetProfileResponseDto> getPetProfiles(UserEntity user) {
		List<PetEntity> petEntityList = petRepository.findAllPetsByUserId(user.getUserId());

		return petEntityList.stream()
			.map(pet -> new PetProfileResponseDto(
				pet.getPetId(),
				pet.getName(),
				pet.getGender(),
				pet.isNeutered(),
				pet.getAge(),
				pet.isAgeKnown(),
				pet.getBreed(),
				pet.getProfileImage() != null ? pet.getProfileImage().getImageUrl() : null,
				pet.getPetTraitSelectedEntityList().stream()
					.map(PetTraitSelectedEntity::getPetTraitOption)
					.map(option -> new PetProfileResponseDto.TraitDto(
						option.getPetTraitCategory().getCategoryName(),
						option.getOptionText()
					))
					.toList(),
				pet.getWalkCount()
			))
			.toList();
	}
}