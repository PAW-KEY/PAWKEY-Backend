package org.sopt.pawkey.backendapi.facade.post;

import org.sopt.pawkey.backendapi.domain.category.application.service.CategoryOptionService;
import org.sopt.pawkey.backendapi.domain.category.application.service.CategoryQueryService;
import org.sopt.pawkey.backendapi.domain.image.application.service.command.ImageService;
import org.sopt.pawkey.backendapi.domain.image.infra.persistence.entity.ImageEntity;
import org.sopt.pawkey.backendapi.domain.post.application.dto.command.PostRegisterCommand;
import org.sopt.pawkey.backendapi.domain.post.application.dto.result.PostRegisterResult;
import org.sopt.pawkey.backendapi.domain.post.application.facade.command.PostRegisterFacade;
import org.sopt.pawkey.backendapi.domain.post.application.service.PostSelectedCategoryOptionService;
import org.sopt.pawkey.backendapi.domain.post.application.service.PostService;
import org.sopt.pawkey.backendapi.domain.post.infra.persistence.entity.PostEntity;
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

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PostRegisterFacadeTest {

    @Mock private UserService userService;
    @Mock private RouteService routeService;
    @Mock private ImageService imageService;
    @Mock private PostService postService;
    @Mock private PostSelectedCategoryOptionService postSelectedCategoryOptionService;
    @Mock private CategoryOptionService categoryOptionService;
    @Mock private CategoryQueryService categoryQueryService;

    @InjectMocks
    private PostRegisterFacade postRegisterFacade;

    @Test
    void 산책_게시물_등록_유즈케이스_정상() {
        // given
        Long userId = 1L;
        Long routeId = 10L;
        Long routeImageId = 100L;
        List<Long> walkImageIds = List.of(201L, 202L);

        UserEntity user = mock(UserEntity.class);
        RouteEntity route = mock(RouteEntity.class);
        PostEntity post = mock(PostEntity.class);

        ImageEntity routeImage = mock(ImageEntity.class);
        ImageEntity walkImage1 = mock(ImageEntity.class);
        ImageEntity walkImage2 = mock(ImageEntity.class);

        PostRegisterCommand command = PostRegisterCommand.builder()
                .title("제목")
                .description("본문")
                .isPublic(true)
                .routeId(routeId)
                .routeImageId(routeImageId)
                .walkImageIds(walkImageIds)
                .selectedOptionsForCategories(List.of())
                .build();

        given(userService.findById(userId)).willReturn(user);
        given(routeService.getRouteById(routeId)).willReturn(route);
        given(route.getRouteId()).willReturn(routeId);


        given(postService.existsByRouteId(anyLong())).willReturn(false);
        given(postService.savePost(user, command, route)).willReturn(post);

        given(imageService.getImageById(routeImageId)).willReturn(routeImage);
        given(imageService.getImageById(201L)).willReturn(walkImage1);
        given(imageService.getImageById(202L)).willReturn(walkImage2);

        // when
        PostRegisterResult result = postRegisterFacade.execute(userId, command);

        // then
        assertThat(result).isNotNull();

        // routeId가 제대로 전달되었는지 검증
        verify(postService).existsByRouteId(eq(routeId));

        verify(post).addRouteImage(routeImage);
        verify(post).addWalkImages(List.of(walkImage1, walkImage2));
    }
}