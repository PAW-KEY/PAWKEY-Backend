package org.sopt.pawkey.backendapi.domain.category.domain.repository;

import java.util.List;

import org.sopt.pawkey.backendapi.domain.category.infra.persistence.entity.CategoryEntity;
import org.springframework.data.jpa.repository.EntityGraph;

public interface CategoryRepository {
	List<CategoryEntity> findAllCategoryWithOptions();
}
