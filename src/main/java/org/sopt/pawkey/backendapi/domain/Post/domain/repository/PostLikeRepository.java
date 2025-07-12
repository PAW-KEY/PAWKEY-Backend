package org.sopt.pawkey.backendapi.domain.post.domain.repository;

import java.util.List;

import org.sopt.pawkey.backendapi.domain.post.infra.persistence.entity.PostLikeEntity;

public interface PostLikeRepository {
	PostLikeEntity save(PostLikeEntity postLike);

	boolean existsByUserIdAndPostId(Long userId, Long postId);

	// fetch join된 결과 조회 메서드 선언
	List<PostLikeEntity> findAllByUserWithPostAndImages(Long userId);
}