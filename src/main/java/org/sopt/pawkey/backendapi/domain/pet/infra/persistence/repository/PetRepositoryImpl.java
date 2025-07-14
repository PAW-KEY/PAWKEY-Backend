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
	private final SpringDataPetRepository petRepository;

	@Override
	public List<PetTraitCategoryEntity> findAllPetTraitCategoriesWithOptions() {

		return springDataPetTraitCategoryRepository.findAllWithOptions();

	}

	@Override
	public List<PetEntity> findAllPetsByUserId(Long userId) {
		return petRepository.findAllByUser_UserId(userId);
	}

}
