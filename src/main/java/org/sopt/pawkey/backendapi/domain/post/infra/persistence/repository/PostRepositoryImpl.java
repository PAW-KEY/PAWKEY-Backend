package org.sopt.pawkey.backendapi.domain.post.infra.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.sopt.pawkey.backendapi.domain.post.domain.repository.PostRepository;
import org.sopt.pawkey.backendapi.domain.post.infra.persistence.entity.PostEntity;
import org.sopt.pawkey.backendapi.domain.routes.infra.persistence.entity.RouteEntity;
import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.UserEntity;
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

	@Override
	public List<PostEntity> findAllByUser(UserEntity user) {
		return jpaRepository.findAllByUser(user);
	}

	@Override
	public Optional<PostEntity> getPostWithAllDetails(Long postId) {
		return jpaRepository.getPostWithAllDetails(postId);
	}

	@Override
	public boolean existsByRouteId(Long routeId) {
		return jpaRepository.existsByRouteRouteId(routeId);
	}

	@Override
	public void increaseLikeCount(Long postId) {
		jpaRepository.increaseLikeCount(postId);
	}

	@Override
	public void decreaseLikeCount(Long postId) {
		jpaRepository.decreaseLikeCount(postId);
	}

	@Override
	public Optional<PostEntity> findByRoute(RouteEntity route) {
		return jpaRepository.findByRoute(route);
	}

	@Override
	public List<PostEntity> findAllByRouteIds(List<Long> routeIds) {
		if (routeIds == null || routeIds.isEmpty()) {
			return List.of();
		}
		return jpaRepository.findByRouteRouteIdInAndIsPublicTrue(routeIds);
	}
}
