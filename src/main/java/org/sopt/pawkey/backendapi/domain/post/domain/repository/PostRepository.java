package org.sopt.pawkey.backendapi.domain.post.domain.repository;

import java.util.List;
import java.util.Optional;

import org.sopt.pawkey.backendapi.domain.post.infra.persistence.entity.PostEntity;
import org.sopt.pawkey.backendapi.domain.routes.infra.persistence.entity.RouteEntity;
import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.UserEntity;

public interface PostRepository {
	Optional<PostEntity> findById(Long postId);

	void save(PostEntity post);

	List<PostEntity> findAllByUser(UserEntity user);

	Optional<PostEntity> getPostWithAllDetails(Long postId);

	boolean existsByRouteId(Long routeId);

	void increaseLikeCount(Long postId);

	void decreaseLikeCount(Long postId);

	Optional<PostEntity> findByRoute(RouteEntity route);
}