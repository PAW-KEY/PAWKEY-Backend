package org.sopt.pawkey.backendapi.domain.post.infra.persistence.repository;

import org.sopt.pawkey.backendapi.domain.post.infra.persistence.entity.PostCategoryOptionTop3Entity;
import org.sopt.pawkey.backendapi.domain.post.infra.persistence.entity.PostEntity;

public interface PostCategoryOptionTop3Repository {
	void deleteByPost(PostEntity post);
	void updateReviewTop3(PostCategoryOptionTop3Entity postCategoryOptionTop3Entity);
}
