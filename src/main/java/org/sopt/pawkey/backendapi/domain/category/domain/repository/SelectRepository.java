package org.sopt.pawkey.backendapi.domain.category.domain.repository;

import java.util.List;

import org.sopt.pawkey.backendapi.domain.category.infra.persistence.entity.SelectEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SelectRepository extends JpaRepository<SelectEntity, Long> {
	@EntityGraph(attributePaths = "selectOptionEntityList")
	@Query("SELECT s FROM SelectEntity s JOIN FETCH s.selectOptionEntityList")
	List<SelectEntity> findAllSelectWithOptionsOrderedById();
}