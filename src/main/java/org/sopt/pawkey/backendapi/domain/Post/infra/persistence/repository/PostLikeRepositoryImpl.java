package org.sopt.pawkey.backendapi.domain.post.infra.persistence.repository;

import java.util.List;

import org.sopt.pawkey.backendapi.domain.post.domain.repository.PostLikeRepository;
import org.sopt.pawkey.backendapi.domain.post.infra.persistence.entity.PostLikeEntity;
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
	public void deleteByUserIdAndPostId(Long userId, Long postId) {
		jpaRepository.deleteByUserIdAndPostIdQuery(userId, postId);
	}

	@Override
	public List<PostLikeEntity> findAllByUserWithPostAndImages(Long userId) {
		return jpaRepository.findAllByUserWithPostAndImages(userId);
	}
}