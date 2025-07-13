package org.sopt.pawkey.backendapi.domain.user.application.service;

import java.util.List;
import java.util.Optional;

import org.sopt.pawkey.backendapi.domain.image.infra.persistence.entity.ImageEntity;
import org.sopt.pawkey.backendapi.domain.pet.api.dto.response.PetProfileResponseDto;
import org.sopt.pawkey.backendapi.domain.pet.infra.persistence.entity.PetTraitSelectedEntity;
import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.UserEntity;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserPetQueryServiceImpl implements UserPetQueryService {

	@Override
	public List<PetProfileResponseDto> getPetProfiles(UserEntity user) {
		return user.getPetEntityList().stream()
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