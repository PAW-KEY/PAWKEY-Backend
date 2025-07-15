package org.sopt.pawkey.backendapi.domain.post.application.service;

import org.sopt.pawkey.backendapi.domain.post.infra.persistence.entity.PostEntity;
import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.UserEntity;

public interface PostLikeService {

	/**
	 * @param user 좋아요를 누른 유저
	 * @param post 좋아요를 누를 게시글
	 */
	void like(final UserEntity user, final PostEntity post);

	void cancelLike(UserEntity user, PostEntity post);
}
