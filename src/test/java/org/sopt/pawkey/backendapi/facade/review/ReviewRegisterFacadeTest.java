package org.sopt.pawkey.backendapi.facade.review;
import org.sopt.pawkey.backendapi.domain.category.application.service.CategoryOptionService;
import org.sopt.pawkey.backendapi.domain.category.application.service.CategoryQueryService;
import org.sopt.pawkey.backendapi.domain.category.infra.persistence.entity.CategoryEntity;
import org.sopt.pawkey.backendapi.domain.category.infra.persistence.entity.CategoryOptionEntity;
import org.sopt.pawkey.backendapi.domain.image.application.service.command.ImageService;
import org.sopt.pawkey.backendapi.domain.image.infra.persistence.entity.ImageEntity;
import org.sopt.pawkey.backendapi.domain.post.application.dto.command.PostRegisterCommand;
import org.sopt.pawkey.backendapi.domain.post.application.dto.result.PostRegisterResult;
import org.sopt.pawkey.backendapi.domain.post.application.facade.command.PostRegisterFacade;
import org.sopt.pawkey.backendapi.domain.post.application.service.PostSelectedCategoryOptionService;
import org.sopt.pawkey.backendapi.domain.post.application.service.PostService;
import org.sopt.pawkey.backendapi.domain.post.infra.persistence.entity.PostEntity;
import org.sopt.pawkey.backendapi.domain.review.application.dto.SelectedReviewSet;
import org.sopt.pawkey.backendapi.domain.review.application.dto.command.ReviewRegisterCommand;
import org.sopt.pawkey.backendapi.domain.review.application.facade.ReviewRegisterFacade;
import org.sopt.pawkey.backendapi.domain.review.application.service.ReviewCategoryOptionTop3Service;
import org.sopt.pawkey.backendapi.domain.review.application.service.ReviewService;
import org.sopt.pawkey.backendapi.domain.user.application.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sopt.pawkey.backendapi.domain.routes.application.service.RouteService;
import org.sopt.pawkey.backendapi.domain.routes.infra.persistence.entity.RouteEntity;
import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.UserEntity;

import java.util.List;


import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
@ExtendWith(MockitoExtension.class)
class ReviewRegisterFacadeTest {

    @Mock private ReviewService reviewService;
    @Mock private UserService userService;
    @Mock private RouteService routeService;
    @Mock private ReviewCategoryOptionTop3Service reviewCategoryOptionTop3Service;
    @Mock private CategoryOptionService categoryOptionService;
    @Mock private CategoryQueryService categoryQueryService;

    @InjectMocks
    private ReviewRegisterFacade reviewRegisterFacade;

    @Test
    void 리뷰_등록_유즈케이스_정상() {
        // given
        Long userId = 1L;
        Long routeId = 10L;

        UserEntity user = mock(UserEntity.class);
        RouteEntity route = mock(RouteEntity.class);
        CategoryOptionEntity option = mock(CategoryOptionEntity.class);
        CategoryEntity category = mock(CategoryEntity.class);

        given(option.getCategory()).willReturn(category);
        given(userService.findById(userId)).willReturn(user);
        given(routeService.getRouteById(routeId)).willReturn(route);
        given(categoryOptionService.getAllWhereInIds(anyList())).willReturn(List.of(option));
        given(categoryQueryService.getAllCategoryEntitiesWithOptions()).willReturn(List.of(category));

        ReviewRegisterCommand command = ReviewRegisterCommand.builder()
                .routeId(routeId)
                .selectedReviewSetList(List.of(
                        new SelectedReviewSet(1L, List.of(1L))
                ))
                .build();

        // when
        reviewRegisterFacade.execute(userId, command);

        // then
        verify(reviewService).saveReview(eq(command), eq(user), eq(route), anyList());
    }
}