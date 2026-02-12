package org.sopt.pawkey.backendapi.domain.post.application.service;

import org.sopt.pawkey.backendapi.domain.post.domain.repository.PostLikeRepository;
import org.sopt.pawkey.backendapi.domain.post.domain.repository.PostRepository;
import org.sopt.pawkey.backendapi.domain.post.exception.PostLikeBusinessException;
import org.sopt.pawkey.backendapi.domain.post.exception.PostLikeErrorCode;
import org.sopt.pawkey.backendapi.domain.post.infra.persistence.entity.PostEntity;
import org.sopt.pawkey.backendapi.domain.post.infra.persistence.entity.PostLikeEntity;
import org.sopt.pawkey.backendapi.domain.post.infra.persistence.repository.SpringDataPostRepository;
import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.UserEntity;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostLikeService {

	private final PostLikeRepository postLikeRepository;
	private final PostRepository postRepository;

	public void like(final UserEntity user, final PostEntity post) {
		if (postLikeRepository.existsByUserIdAndPostId(user.getUserId(), post.getPostId())) {
			throw new PostLikeBusinessException(PostLikeErrorCode.DUPLICATE_LIKE);
		}

		postLikeRepository.save(PostLikeEntity.builder()
			.post(post)
			.user(user)
			.build());

		postRepository.increaseLikeCount(post.getPostId());
	}

	public void cancelLike(UserEntity user, PostEntity post) {
		int deletedCount = postLikeRepository.deleteByUserIdAndPostId(user.getUserId(), post.getPostId());

		if (deletedCount == 0) {
			throw new PostLikeBusinessException(PostLikeErrorCode.LIKE_NOT_FOUND);
		}

		postRepository.decreaseLikeCount(post.getPostId());
	}
}