package org.sopt.pawkey.backendapi.domain.post.application.facade.command;

import org.sopt.pawkey.backendapi.domain.image.application.service.command.ImageService;
import org.sopt.pawkey.backendapi.domain.image.domain.model.ImageType;
import org.sopt.pawkey.backendapi.domain.post.application.service.PostService;
import org.sopt.pawkey.backendapi.domain.post.infra.persistence.entity.PostEntity;
import org.sopt.pawkey.backendapi.domain.routes.application.service.RouteService;
import org.sopt.pawkey.backendapi.domain.user.application.service.UserService;
import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.UserEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@Transactional
public class PostDeleteFacade {

	private final PostService postService;
	private final RouteService routeService;
	private final ImageService imageService;
	private final UserService userService;

	public void execute(Long userId, Long postId){
		PostEntity post = postService.findWithImagesForDelete(postId);

		//작성자(게시물 소유권) 검증
		UserEntity user = userService.findById(userId);
		post.validateOwnership(user);

		//산책 이미지 db삭제(s3 삭제는 추후 추가 예정)
		post.getPostImageEntityList().stream()
			.filter(img -> img.getImageType() == ImageType.WALK_POST)
			.forEach(img -> imageService.deleteImageById(img.getImage().getImageId()));


		//루트 이미지 db 삭제
		imageService.deleteImageById(post.getRoute().getTrackingImage().getImageId());


		//게시물 삭제
		postService.delete(post.getPostId());

		//게시물에 매핑된 루트 삭제
		routeService.delete(post.getRoute().getRouteId());







	}
}
