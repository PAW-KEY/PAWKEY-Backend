package org.sopt.pawkey.backendapi.domain.review.infra.persistence;

import java.util.List;

import org.sopt.pawkey.backendapi.domain.review.infra.persistence.entity.ReviewSelectedCategoryOptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SpringDataReviewSelectedCategoryOptionRepository
	extends JpaRepository<ReviewSelectedCategoryOptionEntity, Long> {

	@Query("""
			SELECT r.categoryOption, COUNT(r)
			FROM ReviewSelectedCategoryOptionEntity r
			WHERE r.review.route.routeId = :routeId
			GROUP BY r.categoryOption
			ORDER BY COUNT(r) DESC
		""")
	List<Object[]> countCategoryOptionSelectionsByRoute(@Param("routeId") Long routeId);
}
