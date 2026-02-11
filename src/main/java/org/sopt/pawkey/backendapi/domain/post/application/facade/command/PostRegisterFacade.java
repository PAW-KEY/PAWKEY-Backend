package org.sopt.pawkey.backendapi.domain.post.application.facade.command;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.sopt.pawkey.backendapi.domain.category.application.service.CategoryOptionService;
import org.sopt.pawkey.backendapi.domain.category.application.service.CategoryQueryService;
import org.sopt.pawkey.backendapi.domain.category.exception.CategoryBusinessException;
import org.sopt.pawkey.backendapi.domain.category.exception.CategoryErrorCode;
import org.sopt.pawkey.backendapi.domain.category.infra.persistence.entity.CategoryEntity;
import org.sopt.pawkey.backendapi.domain.category.infra.persistence.entity.CategoryOptionEntity;
import org.sopt.pawkey.backendapi.domain.image.application.service.command.ImageService;
import org.sopt.pawkey.backendapi.domain.image.infra.persistence.entity.ImageEntity;
import org.sopt.pawkey.backendapi.domain.post.application.dto.command.PostRegisterCommand;
import org.sopt.pawkey.backendapi.domain.post.application.dto.command.SelectedOptionsForCategory;
import org.sopt.pawkey.backendapi.domain.post.application.dto.result.PostRegisterResult;
import org.sopt.pawkey.backendapi.domain.post.application.service.PostSelectedCategoryOptionService;
import org.sopt.pawkey.backendapi.domain.post.application.service.PostService;
import org.sopt.pawkey.backendapi.domain.post.exception.PostBusinessException;
import org.sopt.pawkey.backendapi.domain.post.exception.PostErrorCode;
import org.sopt.pawkey.backendapi.domain.post.infra.persistence.entity.PostEntity;
import org.sopt.pawkey.backendapi.domain.routes.application.service.RouteService;
import org.sopt.pawkey.backendapi.domain.routes.infra.persistence.entity.RouteEntity;
import org.sopt.pawkey.backendapi.domain.user.application.service.UserService;
import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.UserEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


import lombok.RequiredArgsConstructor;
@Component
@RequiredArgsConstructor
@Transactional
public class PostRegisterFacade {
	private final UserService userService;
	private final RouteService routeService;
	private final ImageService imageService;
	private final PostService postService;
	private final PostSelectedCategoryOptionService postSelectedCategoryOptionService;
	private final CategoryOptionService categoryOptionService;
	private final CategoryQueryService categoryQueryService;

	public PostRegisterResult execute(Long userId, PostRegisterCommand command) {

		//User와 Route 조회 ( 유스케이스 시작에 필요한 기본 엔티티들)
		UserEntity writer = userService.findById(userId);
		RouteEntity route = routeService.getRouteById(command.routeId());

		// 하나의 루트에는 하나의 게시물만 허용
		throwIfRoutePostExist(route);

		// 게시물 이미지(presined)
		List<ImageEntity> images = command.imageIds().stream()
			.map(imageService::getImageById)
			.peek(ImageEntity::validateUsableForPost)
			.toList();


		PostEntity post = postService.savePost(writer, command, route);

		// Post Aggregate( PostImageEntity 생성 책임-> PostEntity 담당)
		post.addImages(images); //이미지 연결

		processCategorySelection(post, command.selectedOptionsForCategories());


		return PostRegisterResult.builder()
			.postId(post.getPostId())
			.routeId(route.getRouteId())
			.build();
	}
	private void processCategorySelection(
		PostEntity post,
		List<SelectedOptionsForCategory> selectedOptionsForCategories
	) {
		// optionId → CategoryOptionEntity 변환
		List<CategoryOptionEntity> selectedCategoryOptions =
			getCategoryOptionEntities(selectedOptionsForCategories);

		// 카테고리 기준 그룹핑
		Map<CategoryEntity, List<CategoryOptionEntity>> optionsByCategory =
			selectedCategoryOptions.stream()
				.collect(Collectors.groupingBy(CategoryOptionEntity::getCategory));

		// 카테고리 내부 선택 규칙 검증
		optionsByCategory.forEach(
			(category, options) -> category.validateSelection(options)
		);

		// 필수 카테고리 누락 검증
		validateAllRequiredCategoriesSelected(optionsByCategory);

		postSelectedCategoryOptionService.saveSelectedOption(
			post,
			selectedCategoryOptions
		);
	}


	private List<CategoryOptionEntity> getCategoryOptionEntities(
		List<SelectedOptionsForCategory> selectedOptionsForCategories
	) {
		List<Long> selectedOptionIds = selectedOptionsForCategories.stream()
			.flatMap(selectedOptionsForCategory -> selectedOptionsForCategory.getSelectedOptionIds().stream())
			.toList();

		return categoryOptionService.getAllWhereInIds(
			selectedOptionIds);
	}

	private void validateAllRequiredCategoriesSelected( //선택 기준의 검증 -> Facade 책임
		Map<CategoryEntity, List<CategoryOptionEntity>> optionsByCategory
	) {
		List<CategoryEntity> requiredCategories =
			categoryQueryService.getAllCategoryEntitiesWithOptions();

		for (CategoryEntity category : requiredCategories) {
			if (!optionsByCategory.containsKey(category)) {
				throw new CategoryBusinessException(
					CategoryErrorCode.CATEGORY_SELECTION_REQUIRED
				);
			}
		}
	}

	private void throwIfRoutePostExist(RouteEntity route) {
		if (postService.existsByRouteId(route.getRouteId())) {
			throw new PostBusinessException(PostErrorCode.ALREADY_ROUTE_POST_EXIST);
		}
	}
}