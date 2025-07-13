package org.sopt.pawkey.backendapi.domain.post.domain.repository;

import java.util.Optional;

import org.sopt.pawkey.backendapi.domain.post.infra.persistence.entity.PostEntity;
import org.sopt.pawkey.backendapi.domain.post.infra.persistence.entity.PostLikeEntity;

public interface PostRepository {
	Optional<PostEntity> findById(Long postId);
	void save(PostEntity post);
}