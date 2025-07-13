package org.sopt.pawkey.backendapi.domain.post.infra.persistence.repository;

import java.util.Optional;

import org.sopt.pawkey.backendapi.domain.post.domain.repository.PostRepository;
import org.sopt.pawkey.backendapi.domain.post.infra.persistence.entity.PostEntity;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepository {

	private final SpringDataPostRepository jpaRepository;

	@Override
	public Optional<PostEntity> findById(Long postId) {
		return jpaRepository.getByPostId(postId);
	}

	@Override
	public void save(PostEntity post) {
		jpaRepository.save(post);
	}
}