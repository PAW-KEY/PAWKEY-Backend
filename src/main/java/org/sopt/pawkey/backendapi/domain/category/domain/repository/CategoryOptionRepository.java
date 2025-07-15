package org.sopt.pawkey.backendapi.domain.category.domain.repository;

import java.util.Optional;

import org.sopt.pawkey.backendapi.domain.category.infra.persistence.entity.CategoryOptionEntity;

public interface CategoryOptionRepository {

	Optional<CategoryOptionEntity> findById(Long optionId);


}
