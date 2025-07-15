package org.sopt.pawkey.backendapi.domain.review.infra.persistence;

import org.sopt.pawkey.backendapi.domain.review.infra.persistence.entity.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataReviewRepository extends JpaRepository<ReviewEntity, Long> {
}
