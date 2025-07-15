package org.sopt.pawkey.backendapi.domain.review.domain.repository;

import java.util.List;

import org.sopt.pawkey.backendapi.domain.review.infra.persistence.entity.ReviewSelectedCategoryOptionEntity;

public interface ReviewSelectedCategoryOptionRepository {

	void saveAll(List<ReviewSelectedCategoryOptionEntity> selectedCategoryOptionEntityList);

	List<Object[]> countOptionGroupByCategory(Long routeId);
}
