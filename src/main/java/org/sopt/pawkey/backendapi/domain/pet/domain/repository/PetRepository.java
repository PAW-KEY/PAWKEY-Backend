package org.sopt.pawkey.backendapi.domain.pet.domain.repository;

import java.util.List;

import org.sopt.pawkey.backendapi.domain.pet.infra.persistence.entity.PetEntity;
import org.sopt.pawkey.backendapi.domain.pet.infra.persistence.entity.PetTraitCategoryEntity;

public interface PetRepository {
	List<PetTraitCategoryEntity> findAllPetTraitCategoriesWithOptions();

	PetEntity save(PetEntity pet);

	List<PetEntity> findAllPetsByUserId(Long userId);

	List<String> findAllBreeds();
}



