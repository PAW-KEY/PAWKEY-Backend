package org.sopt.pawkey.backendapi.domain.pet.infra.persistence.repository;

import java.util.List;

import org.sopt.pawkey.backendapi.domain.pet.domain.repository.PetRepository;
import org.sopt.pawkey.backendapi.domain.pet.infra.persistence.entity.PetTraitCategoryEntity;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class PetRepositoryImpl implements PetRepository {

	private final SpringDataPetTraitCategoryRepository springDataPetTraitCategoryRepository;

	@Override
	public List<PetTraitCategoryEntity> findAllPetTraitCategoriesWithOptions() {

		return springDataPetTraitCategoryRepository.findAllWithOptions();

	}

}
