package org.sopt.pawkey.backendapi.domain.post.application.facade.command;

import org.sopt.pawkey.backendapi.domain.post.application.service.PostLikeService;
import org.sopt.pawkey.backendapi.domain.post.application.service.PostService;
import org.sopt.pawkey.backendapi.domain.post.infra.persistence.entity.PostEntity;
import org.sopt.pawkey.backendapi.domain.user.application.service.UserService;
import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.UserEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PostLikeFacade {

	private final UserService userService;
	private final PostService postService;
	private final PostLikeService postLikeService;

	@Transactional
	public void like(Long postId, Long userId) {
		final UserEntity user = userService.findById(userId);
		final PostEntity post = postService.findById(postId);

		postLikeService.like(user, post);
	}
}




