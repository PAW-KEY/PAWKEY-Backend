package org.sopt.pawkey.backendapi.facade.review;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sopt.pawkey.backendapi.domain.image.application.service.PresignedImageService;
import org.sopt.pawkey.backendapi.domain.image.infra.persistence.entity.ImageEntity;
import org.sopt.pawkey.backendapi.domain.pet.infra.persistence.entity.PetEntity;
import org.sopt.pawkey.backendapi.domain.post.application.service.PostService;
import org.sopt.pawkey.backendapi.domain.post.infra.persistence.entity.PostEntity;
import org.sopt.pawkey.backendapi.domain.review.application.dto.result.ReviewHeaderResult;
import org.sopt.pawkey.backendapi.domain.review.application.facade.GetReviewHeaderFacade;
import org.sopt.pawkey.backendapi.domain.user.application.service.UserService;
import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.UserEntity;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class GetReviewHeaderFacadeTest {

    @Mock private PostService postService;
    @Mock private UserService userService;
    @Mock private PresignedImageService presignedImageService;

    @InjectMocks
    private GetReviewHeaderFacade getReviewHeaderFacade;

    @Test
    void 리뷰_헤더_조회_성공() {
        // given
        Long userId = 1L;
        Long postId = 10L;

        PostEntity post = mock(PostEntity.class);
        UserEntity user = mock(UserEntity.class);
        PetEntity pet = mock(PetEntity.class);
        ImageEntity profileImage = mock(ImageEntity.class);

        given(postService.findById(postId)).willReturn(post);
        given(userService.findById(userId)).willReturn(user);
        given(user.getPetOrThrow()).willReturn(pet);
        given(post.getTitle()).willReturn("단지와의 룰루랄라");
        given(pet.getName()).willReturn("손민수");
        given(pet.getProfileImage()).willReturn(profileImage);
        given(profileImage.getImageUrl()).willReturn("https://s3.../profile/uuid.jpg");
        given(presignedImageService.createPresignedGetUrl("https://s3.../profile/uuid.jpg"))
                .willReturn("https://presigned.../profile/uuid.jpg");

        // when
        ReviewHeaderResult result = getReviewHeaderFacade.execute(postId, userId);

        // then
        assertThat(result.postTitle()).isEqualTo("단지와의 룰루랄라");
        assertThat(result.profileName()).isEqualTo("손민수");
        assertThat(result.profileImageUrl()).isEqualTo("https://presigned.../profile/uuid.jpg");
    }

    @Test
    void 리뷰_헤더_조회_성공_프로필이미지_없음() {
        // given
        Long userId = 1L;
        Long postId = 10L;

        PostEntity post = mock(PostEntity.class);
        UserEntity user = mock(UserEntity.class);
        PetEntity pet = mock(PetEntity.class);

        given(postService.findById(postId)).willReturn(post);
        given(userService.findById(userId)).willReturn(user);
        given(user.getPetOrThrow()).willReturn(pet);
        given(post.getTitle()).willReturn("단지와의 룰루랄라");
        given(pet.getName()).willReturn("손민수");
        given(pet.getProfileImage()).willReturn(null);

        // when
        ReviewHeaderResult result = getReviewHeaderFacade.execute(postId, userId);

        // then
        assertThat(result.postTitle()).isEqualTo("단지와의 룰루랄라");
        assertThat(result.profileName()).isEqualTo("손민수");
        assertThat(result.profileImageUrl()).isNull();
    }
}