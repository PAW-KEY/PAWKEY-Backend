package org.sopt.pawkey.backendapi.domain.post.infra.persistence.repository;

import org.sopt.pawkey.backendapi.domain.post.infra.persistence.entity.PostCategoryOptionTop3Entity;
import org.sopt.pawkey.backendapi.domain.post.infra.persistence.entity.PostEntity;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class PostCategoryOptionTop3RepositoryImpl implements PostCategoryOptionTop3Repository{

	private final SpringDataPostCategoryOptionTop3Repository jpaRepository;

	public void deleteByPost(PostEntity post) {
		jpaRepository.deleteByPost(post);
	}

	@Override
	public void updateReviewTop3(PostCategoryOptionTop3Entity postCategoryOptionTop3Entity) {
		jpaRepository.save(postCategoryOptionTop3Entity);
	}
}
