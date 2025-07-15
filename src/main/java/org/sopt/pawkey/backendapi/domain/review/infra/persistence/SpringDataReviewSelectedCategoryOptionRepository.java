package org.sopt.pawkey.backendapi.domain.review.infra.persistence;

import org.sopt.pawkey.backendapi.domain.review.infra.persistence.entity.ReviewSelectedCategoryOptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataReviewSelectedCategoryOptionRepository
	extends JpaRepository<ReviewSelectedCategoryOptionEntity, Long> {
}
