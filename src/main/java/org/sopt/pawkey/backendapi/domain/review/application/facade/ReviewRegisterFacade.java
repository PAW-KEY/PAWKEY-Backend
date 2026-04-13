package org.sopt.pawkey.backendapi.domain.review.application.facade;

import org.sopt.pawkey.backendapi.domain.category.application.service.CategoryOptionService;
import org.sopt.pawkey.backendapi.domain.category.application.service.CategoryQueryService;
import org.sopt.pawkey.backendapi.domain.category.exception.CategoryBusinessException;
import org.sopt.pawkey.backendapi.domain.category.exception.CategoryErrorCode;
import org.sopt.pawkey.backendapi.domain.category.infra.persistence.entity.CategoryEntity;
import org.sopt.pawkey.backendapi.domain.category.infra.persistence.entity.CategoryOptionEntity;
import org.sopt.pawkey.backendapi.domain.review.application.dto.command.ReviewRegisterCommand;
import org.sopt.pawkey.backendapi.domain.review.application.service.ReviewCategoryOptionTop3Service;
import org.sopt.pawkey.backendapi.domain.review.application.service.ReviewService;
import org.sopt.pawkey.backendapi.domain.review.infra.persistence.entity.ReviewEntity;
import org.sopt.pawkey.backendapi.domain.routes.application.service.RouteService;
import org.sopt.pawkey.backendapi.domain.routes.infra.persistence.entity.RouteEntity;
import org.sopt.pawkey.backendapi.domain.user.application.service.UserService;
import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.UserEntity;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Transactional
public class ReviewRegisterFacade {

	private final ReviewService reviewService;
	private final UserService userService;
	private final RouteService routeService;
	private final ReviewCategoryOptionTop3Service reviewCategoryOptionTop3Service;

	private final CategoryOptionService categoryOptionService;
	private final CategoryQueryService categoryQueryService;

	public void execute(Long userId, ReviewRegisterCommand command) {

		UserEntity user = userService.findById(userId);
		RouteEntity route = routeService.getRouteById(command.routeId());
		List<Long> selectedOptionIds = command.selectedReviewSetList().stream()
				.flatMap(s -> s.selectedReviewOptionIds().stream())
				.toList();

		List<CategoryOptionEntity> selectedOptions =
				categoryOptionService.getAllWhereInIds(selectedOptionIds);

		Map<CategoryEntity, List<CategoryOptionEntity>> optionsByCategory =
				selectedOptions.stream()
						.collect(Collectors.groupingBy(CategoryOptionEntity::getCategory));

		optionsByCategory.forEach((category, options) ->
				category.validateSelection(options));

		validateAllRequiredCategoriesSelected(optionsByCategory);

		ReviewEntity review = reviewService.saveReview(command, user, route, selectedOptions);

	}
	private void validateAllRequiredCategoriesSelected(
			Map<CategoryEntity, List<CategoryOptionEntity>> optionsByCategory) {
		List<CategoryEntity> requiredCategories =
				categoryQueryService.getAllCategoryEntitiesWithOptions();

		for (CategoryEntity category : requiredCategories) {
			if (!optionsByCategory.containsKey(category)) {
				throw new CategoryBusinessException(
						CategoryErrorCode.CATEGORY_SELECTION_REQUIRED);
			}
		}
	}


}
