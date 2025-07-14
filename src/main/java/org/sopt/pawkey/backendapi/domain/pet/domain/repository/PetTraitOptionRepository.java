package org.sopt.pawkey.backendapi.domain.pet.domain.repository;

import java.util.Optional;

import org.sopt.pawkey.backendapi.domain.pet.infra.persistence.entity.PetTraitOptionEntity;

public interface PetTraitOptionRepository {
	/**
 * Retrieves a pet trait option entity by its unique identifier.
 *
 * @param optionId the unique identifier of the pet trait option
 * @return an {@code Optional} containing the found {@code PetTraitOptionEntity}, or an empty {@code Optional} if not found
 */
Optional<PetTraitOptionEntity> findById(Long optionId);
}
