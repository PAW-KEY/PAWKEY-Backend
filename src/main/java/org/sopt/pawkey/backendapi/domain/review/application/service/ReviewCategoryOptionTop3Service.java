package org.sopt.pawkey.backendapi.domain.review.application.service;

import java.util.List;

import org.sopt.pawkey.backendapi.domain.category.infra.persistence.entity.CategoryOptionEntity;
import org.sopt.pawkey.backendapi.domain.review.domain.repository.ReviewCategoryOptionTop3Repository;
import org.sopt.pawkey.backendapi.domain.review.domain.repository.ReviewSelectedCategoryOptionRepository;
import org.sopt.pawkey.backendapi.domain.review.infra.persistence.entity.ReviewCategoryOptionTop3Entity;
import org.sopt.pawkey.backendapi.domain.routes.infra.persistence.entity.RouteEntity;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewCategoryOptionTop3Service {

	private final ReviewSelectedCategoryOptionRepository reviewSelectedCategoryOptionRepository;
	private final ReviewCategoryOptionTop3Repository reviewCategoryOptionTop3Repository;

	public void recalculateTop3ByRoute(RouteEntity route) {
		reviewCategoryOptionTop3Repository.deleteAllByRoute(route); //기존 데이터 삭제

		//route에 대해 category_option_id별 선택 count 집계
		List<Object[]> countResults = reviewSelectedCategoryOptionRepository.countCategoryOptionSelectionsByRoute(
			route.getRouteId());

		//상위 3개 추출

		countResults.stream()
			.limit(3)
			.map(row -> ReviewCategoryOptionTop3Entity.builder()
				.selectionCount(((Number)row[1]).intValue())
				.route(route)
				.categoryOption((CategoryOptionEntity)row[0])
				.build())
			.forEach(reviewCategoryOptionTop3Repository::save);

	}
}
