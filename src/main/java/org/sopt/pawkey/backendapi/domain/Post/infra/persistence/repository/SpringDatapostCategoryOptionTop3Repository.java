package org.sopt.pawkey.backendapi.domain.post.infra.persistence.repository;

import org.sopt.pawkey.backendapi.domain.post.infra.persistence.entity.PostCategoryOptionTop3Entity;
import org.sopt.pawkey.backendapi.domain.post.infra.persistence.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataPostCategoryOptionTop3Repository extends JpaRepository<PostCategoryOptionTop3Entity,Long> {

	void deleteByPost(PostEntity post);
}
