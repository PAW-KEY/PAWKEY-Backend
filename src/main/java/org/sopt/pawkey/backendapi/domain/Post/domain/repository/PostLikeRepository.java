package org.sopt.pawkey.backendapi.domain.post.domain.repository;

import java.util.List;

import org.sopt.pawkey.backendapi.domain.post.infra.persistence.entity.PostLikeEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostLikeRepository {
	PostLikeEntity save(PostLikeEntity postLike);

	boolean existsByUserIdAndPostId(Long userId, Long postId);

	List<PostLikeEntity> findAllByUserWithPostAndImages(Long userId);

<<<<<<< HEAD
	void deleteByUserIdAndPostId(Long userId, Long postId);

	List<Long> findLikedPostIdsByUserId(Long userId);
=======
	int deleteByUserIdAndPostId(Long userId, Long postId);
>>>>>>> origin/refactor/#193
}