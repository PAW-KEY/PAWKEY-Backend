package org.sopt.pawkey.backendapi.domain.post.domain.repository;

import java.util.List;
import java.util.Optional;

import org.sopt.pawkey.backendapi.domain.post.infra.persistence.entity.PostLikeEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostLikeRepository {
	PostLikeEntity save(PostLikeEntity postLike);

	boolean existsByUserIdAndPostId(Long userId, Long postId);

	List<PostLikeEntity> findAllByUserWithPostAndImages(Long userId);

	void deleteByUserIdAndPostId(Long userId, Long postId);

	List<Long> findLikedPostIdsByUserId(Long userId);
}