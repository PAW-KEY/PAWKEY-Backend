package org.sopt.pawkey.backendapi.domain.review;

import org.sopt.pawkey.backendapi.domain.category.domain.repository.CategoryOptionRepository;
import org.sopt.pawkey.backendapi.domain.category.infra.persistence.entity.CategoryOptionEntity;
import org.sopt.pawkey.backendapi.domain.pet.infra.persistence.entity.PetEntity;
import org.sopt.pawkey.backendapi.domain.post.application.dto.command.PostRegisterCommand;
import org.sopt.pawkey.backendapi.domain.post.application.dto.command.PostUpdateCommand;
import org.sopt.pawkey.backendapi.domain.post.application.service.PostService;
import org.sopt.pawkey.backendapi.domain.post.domain.repository.PostRepository;
import org.sopt.pawkey.backendapi.domain.post.exception.PostBusinessException;
import org.sopt.pawkey.backendapi.domain.post.infra.persistence.entity.PostEntity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sopt.pawkey.backendapi.domain.review.application.dto.command.ReviewRegisterCommand;
import org.sopt.pawkey.backendapi.domain.review.application.service.ReviewService;
import org.sopt.pawkey.backendapi.domain.review.domain.repository.ReviewRepository;
import org.sopt.pawkey.backendapi.domain.review.domain.repository.ReviewSelectedCategoryOptionRepository;
import org.sopt.pawkey.backendapi.domain.review.infra.persistence.entity.ReviewEntity;
import org.sopt.pawkey.backendapi.domain.routes.infra.persistence.entity.RouteEntity;
import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.UserEntity;


import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @Mock private ReviewRepository reviewRepository;
    @Mock private CategoryOptionRepository categoryOptionRepository;
    @Mock private ReviewSelectedCategoryOptionRepository reviewSelectedCategoryOptionRepository;

    @InjectMocks
    private ReviewService reviewService;

    @Test
    void 리뷰_저장_성공() {
        // given
        UserEntity user = mock(UserEntity.class);
        RouteEntity route = mock(RouteEntity.class);

        CategoryOptionEntity option1 = mock(CategoryOptionEntity.class);
        CategoryOptionEntity option2 = mock(CategoryOptionEntity.class);
        List<CategoryOptionEntity> selectedOptions = List.of(option1, option2);

        ReviewRegisterCommand command = ReviewRegisterCommand.builder()
                .routeId(1L)
                .selectedReviewSetList(List.of())
                .build();

        // when
        reviewService.saveReview(command, user, route, selectedOptions);

        // then
        verify(reviewRepository).save(any(ReviewEntity.class));
        verify(reviewSelectedCategoryOptionRepository).saveAll(anyList());
    }
}