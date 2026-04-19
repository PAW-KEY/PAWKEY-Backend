package org.sopt.pawkey.backendapi.domain.review.domain.repository;

import java.util.List;

import org.sopt.pawkey.backendapi.domain.review.infra.persistence.entity.ReviewEntity;

public interface ReviewRepository {

	void save(ReviewEntity review);

	List<ReviewEntity> findAllByUserId(Long userId);

	void deleteByUserId(Long userId);

	void deleteByRouteUserId(Long userId);

	boolean existsByUserIdAndRouteId(Long userId, Long routeId);
}
