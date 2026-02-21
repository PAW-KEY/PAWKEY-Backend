package org.sopt.pawkey.backendapi.domain.post.domain.repository;

import java.util.List;

import org.sopt.pawkey.backendapi.domain.post.infra.persistence.entity.PostEntity;
import org.sopt.pawkey.backendapi.domain.post.infra.persistence.entity.PostSelectedCategoryOptionEntity;

public interface PostSelectedCategoryOptionRepository {
	void saveBatch(List<PostSelectedCategoryOptionEntity> postSelectedCategoryOptions);
	void deleteByPost(PostEntity post);

}
