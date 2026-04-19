//package org.sopt.pawkey.backendapi.domain.post;
//import org.sopt.pawkey.backendapi.domain.image.application.service.PresignedImageService;
//import org.sopt.pawkey.backendapi.domain.image.infra.persistence.entity.ImageEntity;
//
//import org.sopt.pawkey.backendapi.domain.pet.infra.persistence.entity.PetEntity;
//import org.sopt.pawkey.backendapi.domain.post.api.dto.response.PostDetailResponseDto;
//import org.sopt.pawkey.backendapi.domain.post.application.dto.result.GetPostDetailResult;
//import org.sopt.pawkey.backendapi.domain.post.application.dto.result.WalkImageResult;
//import org.sopt.pawkey.backendapi.domain.post.application.facade.query.PostQueryFacade;
//import org.sopt.pawkey.backendapi.domain.post.application.service.PostQueryService;
//import org.sopt.pawkey.backendapi.domain.post.application.service.PostService;
//import org.sopt.pawkey.backendapi.domain.post.infra.persistence.entity.PostEntity;
//
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import org.sopt.pawkey.backendapi.domain.region.infra.persistence.entity.RegionEntity;
//import org.sopt.pawkey.backendapi.domain.routes.infra.persistence.entity.RouteEntity;
//import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.UserEntity;
//import org.sopt.pawkey.backendapi.fixtures.PostFixture;
//import org.sopt.pawkey.backendapi.fixtures.RouteFixture;
//
//
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//import static org.mockito.BDDMockito.given;
//import static org.mockito.Mockito.mock;
//
//@ExtendWith(MockitoExtension.class)
//class PostQueryServiceTest {
//
//    @InjectMocks
//    private PostQueryService postQueryService;
//
//    @Test
//    void 게시물_상세조회_Result_조립_성공() {
//        // given
//        UserEntity user = mock(UserEntity.class);
//        given(user.getUserId()).willReturn(1L);
//
//        PetEntity pet = mock(PetEntity.class);
//        ImageEntity profileImage = mock(ImageEntity.class);
//
//        given(pet.getPetId()).willReturn(2L);
//        given(pet.getName()).willReturn("단지");
//        given(pet.getProfileImage()).willReturn(profileImage);
//        given(profileImage.getImageUrl()).willReturn("https://profile.png");
//
//
//        RouteEntity route = RouteFixture.createRouteForSummary(user);
//        //RegionEntity region = route.getRegion();
//        //given(region.getFullRegionName()).willReturn("강남구 역삼동");
//
//        PostEntity post = PostFixture.createPost(user, route, pet);
//        //given(post.getPostSelectedCategoryOptionEntityList()).willReturn(List.of());
//
//        List<WalkImageResult> walkImages = List.of(
//                new WalkImageResult(1L, "url1"),
//                new WalkImageResult(2L, "url2")
//        );
//
//        // when
//        GetPostDetailResult result =
//                postQueryService.getPostDetailResult(post, true, "route-url", walkImages,false);
//
//        // then
//        assertThat(result).isNotNull();
//        assertThat(result.postId()).isEqualTo(post.getPostId());
//        assertThat(result.isMine()).isTrue();
//        assertThat(result.walkImages()).hasSize(2);
//        assertThat(result.routeDisplay().locationText()).isEqualTo("강남구 역삼동");
//    }
//}