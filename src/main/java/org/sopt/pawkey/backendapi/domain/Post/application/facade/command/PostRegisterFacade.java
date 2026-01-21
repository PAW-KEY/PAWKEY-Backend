package org.sopt.pawkey.backendapi.domain.post.application.facade.command;

import java.util.List;

import org.sopt.pawkey.backendapi.domain.category.application.service.CategoryOptionService;
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
import org.springframework.context.annotation.Profile;
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

	public PostRegisterResult execute(Long userId,
		PostRegisterCommand command) {

		UserEntity writer = userService.findById(userId);
		RouteEntity route = routeService.getRouteById(command.routeId());

		throwIfRoutePostExist(route);

		//List<ImageEntity> imageEntities = imageService.storeWalkPostImages(postImages);

		try {
			PostEntity post = postService.savePost(writer, command, route);

			List<CategoryOptionEntity> selectedCategoryOptions = getCategoryOptionEntities(
				command.selectedOptionsForCategories());
			postSelectedCategoryOptionService.saveSelectedOption(post, selectedCategoryOptions);

			return PostRegisterResult.builder()
				.postId(post.getPostId())
				.routeId(route.getRouteId())
				.build();

		} catch (Exception e) {
			// // 이미지 rollback
			// for (ImageEntity image : imageEntities) {
			// 	imageService.deleteImage(image);
			// }
			throw e;
		}
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

	private void throwIfRoutePostExist(RouteEntity route) {
		if (postService.existsByRouteId(route.getRouteId())) {
			throw new PostBusinessException(PostErrorCode.ALREADY_ROUTE_POST_EXIST);
		}
	}
}