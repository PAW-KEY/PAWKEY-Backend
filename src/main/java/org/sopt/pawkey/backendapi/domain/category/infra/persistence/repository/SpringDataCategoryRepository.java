package org.sopt.pawkey.backendapi.domain.category.infra.persistence.repository;

import java.util.List;

import org.sopt.pawkey.backendapi.domain.category.infra.persistence.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SpringDataCategoryRepository extends JpaRepository<CategoryEntity,Long> {
	@Query("SELECT DISTINCT c FROM CategoryEntity c JOIN FETCH c.categoryOptionEntityList")
	List<CategoryEntity> findAllWithOptions();
}
