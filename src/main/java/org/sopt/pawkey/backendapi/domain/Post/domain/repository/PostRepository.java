package org.sopt.pawkey.backendapi.domain.post.domain.repository;

import java.util.List;
import java.util.Optional;

import org.sopt.pawkey.backendapi.domain.post.infra.persistence.entity.PostEntity;
import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.UserEntity;

public interface PostRepository {
	Optional<PostEntity> findById(Long postId);
	PostEntity findByRouteId(Long routeId);
	void save(PostEntity post);

	List<PostEntity> findAllByUser(UserEntity user);

	Optional<PostEntity> getPostWithAllDetails(Long postId);
}