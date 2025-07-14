package org.sopt.pawkey.backendapi.domain.pet.infra.persistence.repository;

import org.sopt.pawkey.backendapi.domain.pet.domain.repository.PetTraitSelectedRepository;
import org.sopt.pawkey.backendapi.domain.pet.infra.persistence.entity.PetTraitSelectedEntity;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class PetTraitSelectedRepositoryImpl implements PetTraitSelectedRepository {

	private final SpringDataPetTraitSelectedRepository jpaRepository;
	/**
	 * Persists the given PetTraitSelectedEntity to the database.
	 *
	 * @param entity the PetTraitSelectedEntity to be saved
	 */
	@Override
	public void save(PetTraitSelectedEntity entity) {
		jpaRepository.save(entity);
	}
}
