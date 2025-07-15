package org.sopt.pawkey.backendapi.domain.post.infra.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.sopt.pawkey.backendapi.domain.post.domain.repository.PostRepository;
import org.sopt.pawkey.backendapi.domain.post.infra.persistence.entity.PostEntity;
import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.UserEntity;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepository {

	private final SpringDataPostRepository jpaRepository;

	@Override
	public Optional<PostEntity> findById(Long postId) {
		return jpaRepository.findByPostId(postId);
	}

	@Override
	public PostEntity findByRouteId(Long routeId) {
		return jpaRepository.findByRoute_RouteId(routeId);
	}

	@Override
	public void save(PostEntity post) {
		jpaRepository.save(post);
	}

	@Override
	public List<PostEntity> findAllByUser(UserEntity user) {
		return jpaRepository.findAllByUser(user);
	}

	@Override
	public Optional<PostEntity> getPostWithAllDetails(Long postId) {
		return jpaRepository.getPostWithLikesAndImages(postId);
	}
}