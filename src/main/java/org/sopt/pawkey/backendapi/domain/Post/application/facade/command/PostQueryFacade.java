package org.sopt.pawkey.backendapi.domain.post.application.facade.command;

import org.sopt.pawkey.backendapi.domain.image.application.service.command.ImageService;
import org.sopt.pawkey.backendapi.domain.image.infra.persistence.entity.ImageEntity;
import org.sopt.pawkey.backendapi.domain.post.api.dto.response.PostResponseDto;
import org.sopt.pawkey.backendapi.domain.post.application.service.PostService;
import org.sopt.pawkey.backendapi.domain.post.infra.persistence.entity.PostEntity;
import org.sopt.pawkey.backendapi.domain.user.application.service.UserService;
import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.UserEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)

public class PostQueryFacade {

	private final PostQueryService postQueryService;
	private final UserService userService;
	private final PostService postService;
	private final ImageService imageService;


	public PostResponseDto getPostByPostId(Long postId){

		PostEntity post = postService.findById(postId);
		UserEntity writer = userService.findById(post.getUser().getUserId());
		ImageEntity petProfileimage =

	}

}
