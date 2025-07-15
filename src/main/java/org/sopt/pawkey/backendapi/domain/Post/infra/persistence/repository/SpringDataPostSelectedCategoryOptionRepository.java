package org.sopt.pawkey.backendapi.domain.post.infra.persistence.repository;

import org.sopt.pawkey.backendapi.domain.post.infra.persistence.entity.PostSelectedCategoryOptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataPostSelectedCategoryOptionRepository
	extends JpaRepository<PostSelectedCategoryOptionEntity, Long> {
}
