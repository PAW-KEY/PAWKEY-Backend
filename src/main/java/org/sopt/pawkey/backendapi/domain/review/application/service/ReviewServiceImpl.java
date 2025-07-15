package org.sopt.pawkey.backendapi.domain.review.application.service;

import java.util.List;

import org.sopt.pawkey.backendapi.domain.category.domain.repository.CategoryOptionRepository;
import org.sopt.pawkey.backendapi.domain.category.domain.repository.CategoryRepository;
import org.sopt.pawkey.backendapi.domain.category.exception.CategoryBusinessException;
import org.sopt.pawkey.backendapi.domain.category.exception.CategoryErrorCode;
import org.sopt.pawkey.backendapi.domain.category.infra.persistence.entity.CategoryOptionEntity;
import org.sopt.pawkey.backendapi.domain.post.domain.repository.PostRepository;
import org.sopt.pawkey.backendapi.domain.review.application.dto.command.ReviewRegisterCommand;
import org.sopt.pawkey.backendapi.domain.review.domain.repository.ReviewRepository;
import org.sopt.pawkey.backendapi.domain.review.domain.repository.ReviewSelectedCategoryOptionRepository;
import org.sopt.pawkey.backendapi.domain.review.infra.persistence.entity.ReviewEntity;
import org.sopt.pawkey.backendapi.domain.review.infra.persistence.entity.ReviewSelectedCategoryOptionEntity;
import org.sopt.pawkey.backendapi.domain.routes.domain.repository.RouteRepository;
import org.sopt.pawkey.backendapi.domain.routes.infra.persistence.entity.RouteEntity;
import org.sopt.pawkey.backendapi.domain.user.domain.repository.UserRepository;
import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.UserEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewServiceImpl implements ReviewService{
	private final PostRepository postRepository;
	private final RouteRepository routeRepository;
	private final UserRepository userRepository;
	private final ReviewRepository reviewRepository;
	private final CategoryRepository categoryRepository;
	private  final CategoryOptionRepository categoryOptionRepository;
	private final ReviewSelectedCategoryOptionRepository reviewSelectedCategoryOptionRepository;


	@Override
	public ReviewEntity saveReview(ReviewRegisterCommand command, UserEntity user, RouteEntity route) {
		ReviewEntity review = ReviewEntity.builder()
			.user(user)
			.route(route)
			.build();

		reviewRepository.save(review);

		List<ReviewSelectedCategoryOptionEntity> selectedReviewOptionsForCategory = command.selectedReviewSetList().stream()
			.flatMap(selectedReviewSet  -> selectedReviewSet.selectedReviewOptionIds().stream()
				.map(optionId -> {
					CategoryOptionEntity option = categoryOptionRepository.findById(optionId)
						.orElseThrow(()-> new CategoryBusinessException(CategoryErrorCode.CATEGORY_ERROR_CODE));

					return ReviewSelectedCategoryOptionEntity.builder()
						.review(review)
						.categoryOption(option)
						.build();
				})
			)
			.toList();


		reviewSelectedCategoryOptionRepository.saveAll(selectedReviewOptionsForCategory);
		return review;

	}
}

