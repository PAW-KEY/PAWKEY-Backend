package org.sopt.pawkey.backendapi.domain.pet.infra.persistence.repository;

import java.util.List;

import org.sopt.pawkey.backendapi.domain.pet.domain.repository.PetRepository;
import org.sopt.pawkey.backendapi.domain.pet.infra.persistence.entity.PetEntity;
import org.sopt.pawkey.backendapi.domain.pet.infra.persistence.entity.PetTraitCategoryEntity;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class PetRepositoryImpl implements PetRepository {

	private final SpringDataPetTraitCategoryRepository springDataPetTraitCategoryRepository;
	private final SpringDataPetRepository springDataPetRepository;

	/**
	 * Retrieves all pet trait categories along with their associated options.
	 *
	 * @return a list of PetTraitCategoryEntity objects, each including its options
	 */
	@Override
	public List<PetTraitCategoryEntity> findAllPetTraitCategoriesWithOptions() {

		return springDataPetTraitCategoryRepository.findAllWithOptions();

	}

	/**
	 * Persists the given PetEntity and returns the saved instance.
	 *
	 * @param pet the PetEntity to be saved
	 * @return the persisted PetEntity
	 */
	@Override
	public PetEntity save(PetEntity pet) {
		return springDataPetRepository.save(pet);
	}

}
