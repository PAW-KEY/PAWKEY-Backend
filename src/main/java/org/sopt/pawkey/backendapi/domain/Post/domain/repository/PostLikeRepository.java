package org.sopt.pawkey.backendapi.domain.post.domain.repository;

import java.util.List;

import org.sopt.pawkey.backendapi.domain.post.infra.persistence.entity.PostLikeEntity;
import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.UserEntity;

public interface PostLikeRepository {
	PostLikeEntity save(PostLikeEntity postLike);

	boolean existsByUserIdAndPostId(Long userId, Long postId);

	List<PostLikeEntity> findAllByUser(UserEntity user);

	// fetch join된 결과 조회 메서드 선언
	List<PostLikeEntity> findAllByUserWithPostAndImages(UserEntity user);
}