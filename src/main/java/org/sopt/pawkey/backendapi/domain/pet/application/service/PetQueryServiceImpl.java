package org.sopt.pawkey.backendapi.domain.pet.application.service;

import java.util.List;

import org.sopt.pawkey.backendapi.domain.pet.application.dto.response.PetTraitCategoryResult;
import org.sopt.pawkey.backendapi.domain.pet.domain.repository.PetRepository;
import org.sopt.pawkey.backendapi.domain.pet.infra.persistence.entity.PetTraitCategoryEntity;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PetQueryServiceImpl implements PetQueryService {

	private final PetRepository petRepository;

	@Override
	public List<PetTraitCategoryResult> getAllPetTraitCategories() {
		List<PetTraitCategoryEntity> petTraitCategoryEntityList = petRepository.findAllPetTraitCategoriesWithOptions();

		return petTraitCategoryEntityList.stream()
			.map(PetTraitCategoryResult::fromEntity) // static fromDomain() 메서드 있다고 가정
			.toList();
	}
}
