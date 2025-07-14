package org.sopt.pawkey.backendapi.domain.category.infra.persistence.repository;

import java.util.Optional;

import org.sopt.pawkey.backendapi.domain.category.domain.repository.CategoryOptionRepository;
import org.sopt.pawkey.backendapi.domain.category.infra.persistence.entity.CategoryOptionEntity;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CategoryOptionRepositoryImpl implements CategoryOptionRepository {
	private final SpringDataCategoryOptionRepository jpaRepository;

	@Override
	public Optional<CategoryOptionEntity> findById(Long optionId) {
		return jpaRepository.findById(optionId);
	}
}
