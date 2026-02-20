package org.sopt.pawkey.backendapi.domain.category.infra.persistence.repository;

import java.util.List;

import org.sopt.pawkey.backendapi.domain.category.domain.repository.CategoryRepository;
import org.sopt.pawkey.backendapi.domain.category.infra.persistence.entity.CategoryEntity;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CategoryRepositoryImpl implements CategoryRepository {

	private final SpringDataCategoryRepository jpaRepository;

	@Override
	public List<CategoryEntity> findAllCategoryWithOptions() {
		return jpaRepository.findAllWithOptions();
	}

}
