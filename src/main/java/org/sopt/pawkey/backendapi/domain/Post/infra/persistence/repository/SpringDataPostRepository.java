package org.sopt.pawkey.backendapi.domain.post.infra.persistence.repository;

import java.util.Optional;

import org.sopt.pawkey.backendapi.domain.post.infra.persistence.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringDataPostRepository extends JpaRepository<PostEntity, Long> {
	Optional<PostEntity> getByPostId(Long postId);
}
