package org.sopt.pawkey.backendapi.domain.review.infra.persistence;

import org.sopt.pawkey.backendapi.domain.review.infra.persistence.entity.ReviewCategoryOptionTop3Entity;
import org.sopt.pawkey.backendapi.domain.routes.infra.persistence.entity.RouteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface SpringDataReviewCategoryOptionTop3Repository
	extends JpaRepository<ReviewCategoryOptionTop3Entity, Long> {

	@Modifying
	@Transactional
	@Query("DELETE FROM ReviewCategoryOptionTop3Entity r WHERE r.route = :route")
	void deleteAllByRoute(@Param("route") RouteEntity route);

}
