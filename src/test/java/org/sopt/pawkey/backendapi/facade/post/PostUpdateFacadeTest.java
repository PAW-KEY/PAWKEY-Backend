package org.sopt.pawkey.backendapi.facade.post;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.sopt.pawkey.backendapi.domain.category.application.service.CategoryOptionService;
import org.sopt.pawkey.backendapi.domain.category.application.service.CategoryQueryService;
import org.sopt.pawkey.backendapi.domain.image.application.service.command.ImageService;
import org.sopt.pawkey.backendapi.domain.image.infra.persistence.entity.ImageEntity;

import org.sopt.pawkey.backendapi.domain.post.application.dto.command.PostUpdateCommand;
import org.sopt.pawkey.backendapi.domain.post.application.dto.result.PostRegisterResult;

import org.sopt.pawkey.backendapi.domain.post.application.facade.command.PostUpdateFacade;
import org.sopt.pawkey.backendapi.domain.post.application.service.PostSelectedCategoryOptionService;
import org.sopt.pawkey.backendapi.domain.post.application.service.PostService;
import org.sopt.pawkey.backendapi.domain.post.infra.persistence.entity.PostEntity;
import org.sopt.pawkey.backendapi.domain.routes.infra.persistence.entity.RouteEntity;
import org.sopt.pawkey.backendapi.domain.user.application.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.UserEntity;
import java.util.List;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class PostUpdateFacadeTest {

    @Mock
    private UserService userService;

    @Mock
    private PostService postService;

    @Mock
    private ImageService imageService;

    @Mock
    private PostSelectedCategoryOptionService postSelectedCategoryOptionService;

    @Mock
    private CategoryOptionService categoryOptionService;

    @Mock
    private CategoryQueryService categoryQueryService;

    @InjectMocks
    private PostUpdateFacade postUpdateFacade;

    @Test
    void 산책_게시물_수정_유즈케이스_정상() {
        // given
        Long userId = 1L;
        Long postId = 10L;

        UserEntity user = mock(UserEntity.class);
        PostEntity post = mock(PostEntity.class);
        RouteEntity route = mock(RouteEntity.class);

        given(route.getRouteId()).willReturn(10L);
        given(post.getRoute()).willReturn(route);

        PostUpdateCommand command = PostUpdateCommand.builder()
                .title("수정된 제목")
                .description("수정된 본문")
                .isPublic(false)
                .walkImageIds(List.of(201L, 202L))
                .selectedOptionsForCategories(List.of())
                .build();

        ImageEntity image1 = mock(ImageEntity.class);
        ImageEntity image2 = mock(ImageEntity.class);

        given(userService.findById(userId)).willReturn(user);
        given(postService.findById(postId)).willReturn(post);
        given(imageService.getImageById(201L)).willReturn(image1);
        given(imageService.getImageById(202L)).willReturn(image2);

        // when
        PostRegisterResult result =
                postUpdateFacade.execute(userId, postId, command);

        // then
        assertThat(result).isNotNull();
        verify(postService).updatePost(post, user, command);
        verify(post).removeWalkImages();
        verify(post).addWalkImages(anyList());
        verify(postSelectedCategoryOptionService)
                .replaceSelectedOptions(eq(post), anyList());
    }
}