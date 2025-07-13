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
public class PostLikeServiceImpl implements PostLikeService {

	private final PostLikeRepository postLikeRepository;

	@Override
	public void like(final UserEntity user, final PostEntity post) {
		validateNotSelfLike(user, post);

		if (postLikeRepository.existsByUserIdAndPostId(user.getUserId(), post.getPostId())) {
			throw new PostLikeBusinessException(PostLikeErrorCode.DUPLICATE_LIKE);
		}

		postLikeRepository.save(PostLikeEntity.builder()
			.post(post)
			.user(user)
			.build());
	}

	@Override
	public void cancelLike(UserEntity user, PostEntity post) {
		validateNotSelfLike(user, post);

		PostLikeEntity postLike = postLikeRepository
			.findByUserIdAndPostId(user.getUserId(), post.getPostId())
			.orElseThrow(() -> new PostLikeBusinessException(PostLikeErrorCode.LIKE_NOT_FOUND));

		postLikeRepository.delete(postLike);
	}

	private void validateNotSelfLike(UserEntity user, PostEntity post) {
		if (post.getUser().getUserId().equals(user.getUserId())) {
			throw new PostLikeBusinessException(PostLikeErrorCode.CANNOT_LIKE_OWN_POST);
		}
	}
}