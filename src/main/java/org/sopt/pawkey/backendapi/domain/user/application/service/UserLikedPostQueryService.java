package org.sopt.pawkey.backendapi.domain.user.application.service;

import java.util.List;

import org.sopt.pawkey.backendapi.domain.post.infra.persistence.entity.PostLikeEntity;
import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.UserEntity;

public interface UserLikedPostQueryService {
	List<PostLikeEntity> findLikedPostsByUser(UserEntity user);

	List<PostLikeEntity> findLikedPostsByUserWithPostAndImages(UserEntity user);
}
