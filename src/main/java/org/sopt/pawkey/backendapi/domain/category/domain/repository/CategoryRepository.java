package org.sopt.pawkey.backendapi.domain.category.domain.repository;

import java.util.List;

import org.sopt.pawkey.backendapi.domain.category.infra.persistence.entity.CategoryEntity;

public interface CategoryRepository {
	List<CategoryEntity> findAllCategoryWithOptions();

}
