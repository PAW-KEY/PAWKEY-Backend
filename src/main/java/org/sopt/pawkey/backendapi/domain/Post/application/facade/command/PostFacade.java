package org.sopt.pawkey.backendapi.domain.post.application.facade.command;

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
import java.util.List;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PostFacade {
	private final UserService userService;
	private final PostService postService;
	private final PostImageService postImageService;

	@Transactional
	public void createPost(Long userId,
		PostCreateRequestDto requestDto,
		MultipartFile routeImage,
		List<MultipartFile> postImages) throws IOException {

		UserEntity writer = userService.findById(userId);

		String routeImageUrl = null;
		if (routeImage != null && !routeImage.isEmpty()) {
			routeImageUrl = postImageService.saveRouteImage(routeImage);
		}

		List<String> postImageUrlList = List.of();
		if (postImages != null && !postImages.isEmpty()) {
			postImageUrlList = postImageService.savePostImages(postImages);
		}

		PostCreateCommand command = new PostCreateCommand(
			userId,
			requestDto.getTitle(),
			requestDto.getContent(),
			requestDto.getSelectedOptionsForCategories(),
			routeImageUrl,
			postImageUrlList
		);

		postService.createPost(writer, command);
	}
}
