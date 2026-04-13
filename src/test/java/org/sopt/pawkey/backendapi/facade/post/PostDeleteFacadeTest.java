package org.sopt.pawkey.backendapi.facade.post;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sopt.pawkey.backendapi.domain.image.application.service.command.ImageService;
import org.sopt.pawkey.backendapi.domain.image.domain.model.ImageType;
import org.sopt.pawkey.backendapi.domain.image.infra.persistence.entity.ImageEntity;
import org.sopt.pawkey.backendapi.domain.post.application.facade.command.PostDeleteFacade;
import org.sopt.pawkey.backendapi.domain.post.application.service.PostService;
import org.sopt.pawkey.backendapi.domain.post.infra.persistence.entity.PostEntity;
import org.sopt.pawkey.backendapi.domain.post.infra.persistence.entity.PostImageEntity;
import org.sopt.pawkey.backendapi.domain.routes.application.service.RouteService;
import org.sopt.pawkey.backendapi.domain.routes.infra.persistence.entity.RouteEntity;
import org.sopt.pawkey.backendapi.domain.user.application.service.UserService;
import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.UserEntity;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostDeleteFacadeTest {

	@Mock private UserService userService;
	@Mock private PostService postService;
	@Mock private RouteService routeService;
	@Mock private ImageService imageService;

	@InjectMocks
	private PostDeleteFacade postDeleteFacade;

	@Test
	void 산책_게시물_삭제_유즈케이스_정상() {
		// given
		Long userId = 1L;
		Long postId = 10L;
		Long routeId = 20L;
		Long walkImageId = 201L;
		Long routeImageId = 100L;

		UserEntity user = mock(UserEntity.class);
		RouteEntity route = mock(RouteEntity.class);
		PostEntity post = mock(PostEntity.class);

		ImageEntity walkImage = mock(ImageEntity.class);
		ImageEntity routeImage = mock(ImageEntity.class);

		PostImageEntity postImageEntity = mock(PostImageEntity.class);

		given(userService.findById(userId)).willReturn(user);
		given(postService.findWithImagesForDelete(postId)).willReturn(post);

		given(post.getPostId()).willReturn(postId);
		given(post.getRoute()).willReturn(route);
		given(route.getRouteId()).willReturn(routeId);
		given(route.getTrackingImage()).willReturn(routeImage);
		given(routeImage.getImageId()).willReturn(routeImageId);

		given(postImageEntity.getImageType()).willReturn(ImageType.WALK_POST);
		given(postImageEntity.getImage()).willReturn(walkImage);
		given(walkImage.getImageId()).willReturn(walkImageId);
		given(post.getPostImageEntityList()).willReturn(List.of(postImageEntity));

		// when
		postDeleteFacade.execute(userId, postId);

		// then
		verify(imageService).deleteImageById(walkImageId);
		verify(imageService).deleteImageById(routeImageId);
		verify(postService).delete(postId);
		verify(routeService).delete(routeId);
	}
}