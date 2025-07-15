package org.sopt.pawkey.backendapi.domain.category.infra.persistence.repository;

import java.util.List;

import org.sopt.pawkey.backendapi.domain.category.infra.persistence.entity.CategoryOptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SpringDataCategoryOptionRepository extends JpaRepository<CategoryOptionEntity, Long> {

	@Query("SELECT c FROM CategoryOptionEntity c WHERE c.id IN :ids")
	List<CategoryOptionEntity> findByIdIn(@Param("ids") List<Long> ids);
}
