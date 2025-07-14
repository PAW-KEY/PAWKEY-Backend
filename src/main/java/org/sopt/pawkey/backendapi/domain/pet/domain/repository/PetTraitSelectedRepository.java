package org.sopt.pawkey.backendapi.domain.pet.domain.repository;

import org.sopt.pawkey.backendapi.domain.pet.infra.persistence.entity.PetEntity;
import org.sopt.pawkey.backendapi.domain.pet.infra.persistence.entity.PetTraitSelectedEntity;

public interface PetTraitSelectedRepository {
	/**
 * Persists the given PetTraitSelectedEntity to the data store.
 *
 * @param entity the PetTraitSelectedEntity to be saved
 */
void save(PetTraitSelectedEntity entity);
}
