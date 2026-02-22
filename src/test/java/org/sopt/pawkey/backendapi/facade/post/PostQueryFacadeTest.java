package org.sopt.pawkey.backendapi.facade.post;

import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.sopt.pawkey.backendapi.domain.image.application.service.PresignedImageService;
import org.sopt.pawkey.backendapi.domain.image.domain.model.ImageType;
import org.sopt.pawkey.backendapi.domain.image.infra.persistence.entity.ImageEntity;

import org.sopt.pawkey.backendapi.domain.post.api.dto.response.PostDetailResponseDto;
import org.sopt.pawkey.backendapi.domain.post.application.dto.result.GetPostDetailResult;
import org.sopt.pawkey.backendapi.domain.post.application.dto.result.RouteDisplayResult;
import org.sopt.pawkey.backendapi.domain.post.application.dto.result.WalkImageResult;
import org.sopt.pawkey.backendapi.domain.post.application.facade.query.PostQueryFacade;
import org.sopt.pawkey.backendapi.domain.post.application.service.PostQueryService;
import org.sopt.pawkey.backendapi.domain.post.application.service.PostService;
import org.sopt.pawkey.backendapi.domain.post.infra.persistence.entity.PostEntity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.sopt.pawkey.backendapi.domain.post.infra.persistence.entity.PostImageEntity;
import org.sopt.pawkey.backendapi.domain.region.infra.persistence.entity.RegionEntity;
import org.sopt.pawkey.backendapi.domain.routes.infra.persistence.entity.RouteEntity;
import org.sopt.pawkey.backendapi.domain.user.api.dto.AuthorDto;
import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.UserEntity;
import org.sopt.pawkey.backendapi.fixtures.PostFixture;


import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class PostQueryFacadeTest {

    @Mock
    private PostService postService;

    @Mock
    private PostQueryService postQueryService;

    @Mock
    private PresignedImageService presignedImageService;

    @InjectMocks
    private PostQueryFacade postQueryFacade;

    @Test
    void 게시물_상세조회_유즈케이스_정상() {
        // given
        Long userId = 1L;
        Long postId = 10L;

        UserEntity user = mock(UserEntity.class);
        given(user.getUserId()).willReturn(userId);

        // route + route image
        RouteEntity route = mock(RouteEntity.class);
        ImageEntity routeImage = mock(ImageEntity.class);
        RegionEntity region = mock(RegionEntity.class);

        given(route.getRouteId()).willReturn(10L);
        given(route.getTrackingImage()).willReturn(routeImage);
        given(routeImage.getImageUrl()).willReturn("s3://route.png");
        given(route.getRegion()).willReturn(region);
        given(region.getFullRegionName()).willReturn("강남구 역삼동");
        given(route.getStartedAt()).willReturn(LocalDateTime.now());
        given(route.getDistance()).willReturn(2200);
        given(route.getDurationMinutes()).willReturn(20);
        given(route.getStepCount()).willReturn(1500);

        given(presignedImageService.createPresignedGetUrl(anyString()))
                .willReturn("https://presigned-url");

        PostEntity post = mock(PostEntity.class);
        given(post.getUser()).willReturn(user);
        given(post.getRoute()).willReturn(route);

        PostImageEntity postImage = mock(PostImageEntity.class);
        ImageEntity walkImage = mock(ImageEntity.class);

        given(postImage.getImageType()).willReturn(ImageType.WALK_POST);
        given(postImage.getImage()).willReturn(walkImage);
        given(walkImage.getImageId()).willReturn(1L);
        given(walkImage.getImageUrl()).willReturn("s3://walk.png");

        given(post.getPostImageEntityList()).willReturn(List.of(postImage));
        given(post.getPostSelectedCategoryOptionEntityList()).willReturn(List.of());

        given(postService.findByIdWithAllDetails(postId)).willReturn(post);


        AuthorDto authorDto = new AuthorDto(
                userId,
                1L,
                "단지",
                "https://pet.png"
        );

        RouteDisplayResult routeDisplayResult = RouteDisplayResult.builder()
                .routeId(10L)
                .locationText("강남구 역삼동")
                .dateTimeText("2025-01-01 10:00")
                .metaTagTexts(List.of("2.2km", "20분", "1500걸음"))
                .routeImageUrl("https://presigned-url")
                .build();

        GetPostDetailResult result = GetPostDetailResult.builder()
                .postId(postId)
                .title("제목")
                .description("본문")
                .isPublic(true)
                .isMine(true)
                .authorInfo(authorDto)
                .routeDisplay(routeDisplayResult)
                .categoryTagTexts(List.of("경치좋음"))
                .walkImages(List.of(
                        new WalkImageResult(1L, "https://presigned-url")
                ))
                .build();

        given(postQueryService.getPostDetailResult(any(), anyBoolean(), any(), any()))
                .willReturn(result);

        // when
        PostDetailResponseDto response = postQueryFacade.getPostDetail(postId, userId);

        // then
        assertThat(response).isNotNull();
        verify(postService).findByIdWithAllDetails(postId);
        verify(postQueryService).getPostDetailResult(
                eq(post),
                eq(true),
                eq("https://presigned-url"),
                any()
        );
    }
}