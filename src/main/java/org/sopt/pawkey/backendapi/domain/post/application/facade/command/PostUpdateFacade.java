package org.sopt.pawkey.backendapi.domain.post.application.facade.command;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

import org.sopt.pawkey.backendapi.domain.category.application.service.CategoryOptionService;
import org.sopt.pawkey.backendapi.domain.category.application.service.CategoryQueryService;
import org.sopt.pawkey.backendapi.domain.image.application.service.command.ImageService;
import org.sopt.pawkey.backendapi.domain.image.infra.persistence.entity.ImageEntity;

import org.sopt.pawkey.backendapi.domain.post.application.dto.command.PostUpdateCommand;
import org.sopt.pawkey.backendapi.domain.post.application.dto.result.PostRegisterResult;
import org.sopt.pawkey.backendapi.domain.post.application.service.PostSelectedCategoryOptionService;
import org.sopt.pawkey.backendapi.domain.post.application.service.PostService;

import org.sopt.pawkey.backendapi.domain.post.infra.persistence.entity.PostEntity;

import org.sopt.pawkey.backendapi.domain.user.application.service.UserService;
import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.UserEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.sopt.pawkey.backendapi.domain.category.exception.CategoryBusinessException;
import org.sopt.pawkey.backendapi.domain.category.exception.CategoryErrorCode;
import org.sopt.pawkey.backendapi.domain.category.infra.persistence.entity.CategoryEntity;
import org.sopt.pawkey.backendapi.domain.category.infra.persistence.entity.CategoryOptionEntity;
import org.sopt.pawkey.backendapi.domain.post.application.dto.command.SelectedOptionsForCategory;

@Component
@RequiredArgsConstructor
@Transactional
public class PostUpdateFacade {

    private final UserService userService;
    private final PostService postService;
    private final ImageService imageService;
    private final PostSelectedCategoryOptionService postSelectedCategoryOptionService;
    private final CategoryOptionService categoryOptionService;
    private final CategoryQueryService categoryQueryService;

    public PostRegisterResult execute(Long userId, Long postId, PostUpdateCommand command) {


        UserEntity user = userService.findById(userId);
        PostEntity post = postService.findById(postId);

        postService.updatePost(post, user, command);

        post.removeWalkImages();

        List<ImageEntity> walkImages = command.walkImageIds() == null
                ? List.of()
                : command.walkImageIds().stream()
                .map(imageService::getImageById)
                .peek(ImageEntity::validateUsableForPost)
                .toList();

        post.addWalkImages(walkImages);

        // 5. 카테고리 선택 교체
        processCategorySelectionForUpdate(post, command.selectedOptionsForCategories());

        return PostRegisterResult.builder()
                .postId(post.getPostId())
                .routeId(post.getRoute().getRouteId())
                .build();
    }

    //게시물 등록과 동일한 규칙 재사용
    private void processCategorySelectionForUpdate(
            PostEntity post,
            List<SelectedOptionsForCategory> selectedOptionsForCategories
    ) {
        List<CategoryOptionEntity> selectedCategoryOptions =
                getCategoryOptionEntities(selectedOptionsForCategories);

        Map<CategoryEntity, List<CategoryOptionEntity>> optionsByCategory =
                selectedCategoryOptions.stream()
                        .collect(Collectors.groupingBy(CategoryOptionEntity::getCategory));

        optionsByCategory.forEach(
                (category, options) -> category.validateSelection(options)
        );

        validateAllRequiredCategoriesSelected(optionsByCategory);

        postSelectedCategoryOptionService.replaceSelectedOptions(
                post, selectedCategoryOptions
        );
    }

    private List<CategoryOptionEntity> getCategoryOptionEntities(
            List<SelectedOptionsForCategory> selectedOptionsForCategories
    ) {
        List<Long> selectedOptionIds = selectedOptionsForCategories.stream()
                .flatMap(sel -> sel.getSelectedOptionIds().stream())
                .toList();

        return categoryOptionService.getAllWhereInIds(selectedOptionIds);
    }

    private void validateAllRequiredCategoriesSelected(
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

}
