package org.sopt.pawkey.backendapi.domain.user.application.service;

import java.util.List;

import org.sopt.pawkey.backendapi.domain.post.infra.persistence.entity.PostLikeEntity;

public interface UserLikedPostQueryService {
	List<PostLikeEntity> findLikedPostsByUserWithPostAndImages(Long userId);
}
