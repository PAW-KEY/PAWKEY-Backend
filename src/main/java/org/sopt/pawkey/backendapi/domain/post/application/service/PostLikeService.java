package org.sopt.pawkey.backendapi.domain.post.application.service;

import org.sopt.pawkey.backendapi.domain.post.api.dto.response.PostLikeResponse;
import org.sopt.pawkey.backendapi.domain.post.domain.repository.PostLikeRepository;
import org.sopt.pawkey.backendapi.domain.post.domain.repository.PostRepository;
import org.sopt.pawkey.backendapi.domain.post.infra.persistence.entity.PostEntity;
import org.sopt.pawkey.backendapi.domain.post.infra.persistence.entity.PostLikeEntity;
import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.UserEntity;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostLikeService {

	private final PostLikeRepository postLikeRepository;
	private final PostRepository postRepository;

	public PostLikeResponse toggleLike(final UserEntity user, final PostEntity post) {
		boolean isLiked = postLikeRepository.existsByUserIdAndPostId(user.getUserId(), post.getPostId());

		if (isLiked) {
			postLikeRepository.deleteByUserIdAndPostId(user.getUserId(), post.getPostId());
			postRepository.decreaseLikeCount(post.getPostId());

			return PostLikeResponse.of(PostLikeResponse.CANCEL_SUCCESS);
		} else {
			postLikeRepository.save(PostLikeEntity.builder()
				.post(post)
				.user(user)
				.build());
			postRepository.increaseLikeCount(post.getPostId());

			return PostLikeResponse.of(PostLikeResponse.LIKE_SUCCESS);
		}
	}
}