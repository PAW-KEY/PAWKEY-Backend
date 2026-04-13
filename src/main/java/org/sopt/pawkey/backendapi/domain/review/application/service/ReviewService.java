package org.sopt.pawkey.backendapi.domain.review.application.service;

import java.util.List;

import org.sopt.pawkey.backendapi.domain.category.domain.repository.CategoryOptionRepository;
import org.sopt.pawkey.backendapi.domain.category.exception.CategoryBusinessException;
import org.sopt.pawkey.backendapi.domain.category.exception.CategoryErrorCode;
import org.sopt.pawkey.backendapi.domain.category.infra.persistence.entity.CategoryOptionEntity;
import org.sopt.pawkey.backendapi.domain.review.application.dto.command.ReviewRegisterCommand;
import org.sopt.pawkey.backendapi.domain.review.domain.repository.ReviewRepository;
import org.sopt.pawkey.backendapi.domain.review.domain.repository.ReviewSelectedCategoryOptionRepository;
import org.sopt.pawkey.backendapi.domain.review.infra.persistence.entity.ReviewEntity;
import org.sopt.pawkey.backendapi.domain.review.infra.persistence.entity.ReviewSelectedCategoryOptionEntity;
import org.sopt.pawkey.backendapi.domain.routes.infra.persistence.entity.RouteEntity;
import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.UserEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewService {

	private final ReviewRepository reviewRepository;
	private final CategoryOptionRepository categoryOptionRepository;
	private final ReviewSelectedCategoryOptionRepository reviewSelectedCategoryOptionRepository;

	public ReviewEntity saveReview(ReviewRegisterCommand command, UserEntity user, RouteEntity route,List<CategoryOptionEntity> selectedOptions) {
		ReviewEntity review = ReviewEntity.builder()
			.user(user)
			.route(route)
			.build();

		reviewRepository.save(review); //리뷰 저장

		List<ReviewSelectedCategoryOptionEntity> selectedReviewOptions = selectedOptions.stream()
				.map(option -> ReviewSelectedCategoryOptionEntity.builder()
						.review(review)
						.categoryOption(option)
						.build())
				.toList();

		reviewSelectedCategoryOptionRepository.saveAll(selectedReviewOptions);
		return review;

	}
}

