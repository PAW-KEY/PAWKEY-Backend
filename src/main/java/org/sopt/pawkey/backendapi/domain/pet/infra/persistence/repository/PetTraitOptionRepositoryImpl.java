package org.sopt.pawkey.backendapi.domain.pet.infra.persistence.repository;


import java.util.Optional;

import org.sopt.pawkey.backendapi.domain.pet.domain.repository.PetTraitOptionRepository;
import org.sopt.pawkey.backendapi.domain.pet.infra.persistence.entity.PetTraitOptionEntity;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class PetTraitOptionRepositoryImpl implements PetTraitOptionRepository {

	private final SpringDataPetTraitOptionRepository jpaRepository;

	/**
	 * Retrieves a pet trait option entity by its unique identifier.
	 *
	 * @param optionId the unique identifier of the pet trait option
	 * @return an {@code Optional} containing the found {@code PetTraitOptionEntity}, or empty if not found
	 */
	@Override
	public Optional<PetTraitOptionEntity> findById(Long optionId) {
		return jpaRepository.findById(optionId);
	}
}
