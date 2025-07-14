package org.sopt.pawkey.backendapi.domain.pet.domain.repository;

import java.util.List;

import org.sopt.pawkey.backendapi.domain.pet.infra.persistence.entity.PetEntity;
import org.sopt.pawkey.backendapi.domain.pet.infra.persistence.entity.PetTraitCategoryEntity;

public interface PetRepository {
	/**
 * Retrieves all pet trait categories along with their associated options.
 *
 * @return a list of pet trait categories, each including its available options
 */
List<PetTraitCategoryEntity> findAllPetTraitCategoriesWithOptions();

	/**
 * Persists the given pet entity and returns the saved instance.
 *
 * @param pet the pet entity to be saved
 * @return the persisted pet entity
 */
PetEntity save(PetEntity pet);
}


