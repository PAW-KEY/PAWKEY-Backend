package org.sopt.pawkey.backendapi.domain.review.domain.repository;

import java.util.List;

import org.sopt.pawkey.backendapi.domain.review.infra.persistence.entity.ReviewSelectedCategoryOptionEntity;
import org.sopt.pawkey.backendapi.domain.routes.infra.persistence.entity.RouteEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewSelectedCategoryOptionRepository {

	void saveAll(List<ReviewSelectedCategoryOptionEntity> selectedCategoryOptionEntityList);
	List<Object[]> countCategoryOptionSelectionsByRoute(Long routeId);
}
