package org.sopt.pawkey.backendapi.domain.review.infra.persistence;

import org.sopt.pawkey.backendapi.domain.review.domain.repository.ReviewCachingRepository;
import org.sopt.pawkey.backendapi.domain.review.infra.persistence.entity.ReviewCategoryOptionTop3Entity;
import org.sopt.pawkey.backendapi.domain.routes.infra.persistence.entity.RouteEntity;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ReviewCachingRepositoryImpl implements ReviewCachingRepository {
	private final SpringDataReviewCategoryOptionTop3Repository jpaRepository;

	@Override
	public void deleteAllByRoute(RouteEntity route) {
		jpaRepository.deleteAllByRoute(route);
	}

	@Override
	public void save(ReviewCategoryOptionTop3Entity entity) {
		jpaRepository.save(entity);
	}


}
