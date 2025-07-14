package org.sopt.pawkey.backendapi.domain.category.infra.persistence.repository;

import org.sopt.pawkey.backendapi.domain.category.infra.persistence.entity.CategoryOptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataCategoryOptionRepository extends JpaRepository<CategoryOptionEntity,Long> {
}
