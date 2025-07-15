package org.sopt.pawkey.backendapi.domain.review.infra.persistence;

import java.util.List;

import org.sopt.pawkey.backendapi.domain.review.domain.repository.ReviewSelectedCategoryOptionRepository;
import org.sopt.pawkey.backendapi.domain.review.infra.persistence.entity.ReviewSelectedCategoryOptionEntity;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ReviewSelectedCategoryOptionRepositoryImpl implements ReviewSelectedCategoryOptionRepository {

	private final SpringDataReviewSelectedCategoryOptionRepository jpaRepository;

	@Override
	public void saveAll(List<ReviewSelectedCategoryOptionEntity> selectedCategoryOptionEntityList) {
		jpaRepository.saveAll(selectedCategoryOptionEntityList);
	}

	@Override
	public List<Object[]> countOptionGroupByCategory(Long routeId) {
		return jpaRepository.countOptionGroupByCategory(routeId);
	}
}
