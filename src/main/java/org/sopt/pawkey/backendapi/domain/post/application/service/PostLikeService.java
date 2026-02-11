package org.sopt.pawkey.backendapi.domain.post.application.service;

import org.sopt.pawkey.backendapi.domain.post.domain.repository.PostLikeRepository;
import org.sopt.pawkey.backendapi.domain.post.exception.PostLikeBusinessException;
import org.sopt.pawkey.backendapi.domain.post.exception.PostLikeErrorCode;
import org.sopt.pawkey.backendapi.domain.post.infra.persistence.entity.PostEntity;
import org.sopt.pawkey.backendapi.domain.post.infra.persistence.entity.PostLikeEntity;
import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.UserEntity;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostLikeService {

	private final PostLikeRepository postLikeRepository;

	public void like(final UserEntity user, final PostEntity post) {
		if (postLikeRepository.existsByUserIdAndPostId(user.getUserId(), post.getPostId())) {
			throw new PostLikeBusinessException(PostLikeErrorCode.DUPLICATE_LIKE);
		}

		PostLikeEntity postLike = PostLikeEntity.builder()
			.post(post)
			.user(user)
			.build();

		post.addPostLike(postLike);
		postLikeRepository.save(postLike);
	}

	public void cancelLike(UserEntity user, PostEntity post) {
		PostLikeEntity postLike = post.getPostLikeEntityList().stream()
			.filter(like -> like.getUser().getUserId().equals(user.getUserId()))
			.findFirst()
			.orElseThrow(() -> new PostLikeBusinessException(PostLikeErrorCode.LIKE_NOT_FOUND));

		post.removePostLike(postLike);

		postLikeRepository.deleteByUserIdAndPostId(user.getUserId(), post.getPostId());
	}
}