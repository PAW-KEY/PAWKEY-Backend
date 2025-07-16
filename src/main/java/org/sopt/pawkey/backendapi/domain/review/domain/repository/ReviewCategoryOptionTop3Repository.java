package org.sopt.pawkey.backendapi.domain.review.domain.repository;

import org.sopt.pawkey.backendapi.domain.review.infra.persistence.entity.ReviewCategoryOptionTop3Entity;
import org.sopt.pawkey.backendapi.domain.routes.infra.persistence.entity.RouteEntity;

public interface ReviewCategoryOptionTop3Repository {
	void deleteAllByRoute(RouteEntity route);
	void save(ReviewCategoryOptionTop3Entity entity);
}
