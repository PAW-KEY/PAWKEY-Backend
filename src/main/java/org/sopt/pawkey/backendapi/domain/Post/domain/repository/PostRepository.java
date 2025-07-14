package org.sopt.pawkey.backendapi.domain.post.domain.repository;

import java.util.List;
import java.util.Optional;

import org.sopt.pawkey.backendapi.domain.post.infra.persistence.entity.PostEntity;
import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.UserEntity;

public interface PostRepository {
	Optional<PostEntity> findById(Long postId);

	void save(PostEntity post);

	List<PostEntity> findAllByWriter(UserEntity user);
}