// PostLikeRepositoryImpl.java
package org.sopt.pawkey.backendapi.domain.post.infra.persistence.repository;

import java.util.List;

import org.sopt.pawkey.backendapi.domain.post.domain.repository.PostLikeRepository;
import org.sopt.pawkey.backendapi.domain.post.infra.persistence.entity.PostLikeEntity;
import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.UserEntity;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class PostLikeRepositoryImpl implements PostLikeRepository {

	private final SpringDataPostLikeRepository jpaRepository;

	@Override
	public PostLikeEntity save(PostLikeEntity postLike) {

		return jpaRepository.save(postLike); // Entity → Domain
	}

	@Override
	public boolean existsByUserIdAndPostId(Long userId, Long postId) {
		return jpaRepository.existsByUser_UserIdAndPost_PostId(userId, postId);
	}

	@Override
	public List<PostLikeEntity> findAllByUser(UserEntity user) {
		return jpaRepository.findAllByUser(user);
	}
}