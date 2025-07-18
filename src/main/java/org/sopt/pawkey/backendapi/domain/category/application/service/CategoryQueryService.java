package org.sopt.pawkey.backendapi.domain.category.application.service;

import java.util.List;

import org.sopt.pawkey.backendapi.domain.category.application.dto.result.CategoryResult;
import org.sopt.pawkey.backendapi.domain.category.domain.repository.CategoryRepository;
import org.sopt.pawkey.backendapi.domain.category.infra.persistence.entity.CategoryEntity;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryQueryService {
	private final CategoryRepository categoryRepository;
	
	public List<CategoryResult> getAllCategories() {
		List<CategoryEntity> categoryEntityList = categoryRepository.findAllCategoryWithOptions();
		return categoryEntityList.stream()
			.map(CategoryResult::fromEntity)
			.toList();
	}

	public List<CategoryResult> getAllCategoriesSummary() {
		List<CategoryEntity> categoryEntityList = categoryRepository.findAllCategoryWithOptions();
		return categoryEntityList.stream()
			.map(CategoryResult::fromEntityWithSummary)
			.toList();
	}
}
