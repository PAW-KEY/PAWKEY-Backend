package org.sopt.pawkey.backendapi.domain.review.domain.repository;

import org.sopt.pawkey.backendapi.domain.review.infra.persistence.entity.ReviewEntity;

public interface ReviewRepository {

	void save(ReviewEntity review);

}
