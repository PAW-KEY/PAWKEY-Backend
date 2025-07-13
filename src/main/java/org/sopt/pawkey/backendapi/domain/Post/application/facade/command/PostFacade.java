package org.sopt.pawkey.backendapi.domain.post.application.facade.command;

import org.sopt.pawkey.backendapi.domain.common.ImageStorage;
import org.sopt.pawkey.backendapi.domain.post.api.dto.request.PostCreateRequestDto;
import org.sopt.pawkey.backendapi.domain.post.application.dto.command.PostCreateCommand;
import org.sopt.pawkey.backendapi.domain.post.application.service.PostImageService;
import org.sopt.pawkey.backendapi.domain.post.application.service.PostService;
import org.sopt.pawkey.backendapi.domain.user.application.service.UserService;
import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.UserEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PostFacade {
	private final UserService userService;
	private final PostService postService;
	private final ImageStorage imageStorage;

	@Transactional
	public void createPost(Long userId,
		PostCreateRequestDto requestDto,
		MultipartFile routeImage,
		List<MultipartFile> postImages) {


		UserEntity writer = userService.findById(userId);


		final String routeImageUrl = (routeImage != null && !routeImage.isEmpty())
			? imageStorage.uploadRouteImage(routeImage)
			: null;

		final List<String> postImageUrlList = (postImages != null && !postImages.isEmpty())
			? imageStorage.uploadWalkImages(postImages)
			: Collections.emptyList();


		PostCreateCommand command = new PostCreateCommand(
			userId,
			requestDto.getTitle(),
			requestDto.getDescription(),
			requestDto.isPublic(),
			requestDto.getSelectedOptionsForCategories(),
			routeImageUrl,
			postImageUrlList,
			requestDto.getRouteId()

		);

		// 4. 게시물 생성
		postService.createPost(writer,command);
	}
}
