package org.sopt.pawkey.backendapi.domain.post.domain.repository;

import java.util.List;
import java.util.Optional;

import org.sopt.pawkey.backendapi.domain.post.infra.persistence.entity.PostLikeEntity;

public interface PostLikeRepository {
	PostLikeEntity save(PostLikeEntity postLike);

	boolean existsByUserIdAndPostId(Long userId, Long postId);

	Optional<PostLikeEntity> findByUserIdAndPostId(Long userId, Long postId);

	void delete(PostLikeEntity postLike);

	List<PostLikeEntity> findAllByUserWithPostAndImages(Long userId);
}