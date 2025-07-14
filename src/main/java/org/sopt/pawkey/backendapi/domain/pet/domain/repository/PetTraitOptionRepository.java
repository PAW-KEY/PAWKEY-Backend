package org.sopt.pawkey.backendapi.domain.pet.domain.repository;

import java.util.Optional;

import org.sopt.pawkey.backendapi.domain.pet.infra.persistence.entity.PetTraitOptionEntity;

public interface PetTraitOptionRepository {
	Optional<PetTraitOptionEntity> findById(Long optionId);
}
