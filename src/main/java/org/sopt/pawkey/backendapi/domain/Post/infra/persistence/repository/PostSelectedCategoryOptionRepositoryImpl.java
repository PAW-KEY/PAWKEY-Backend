package org.sopt.pawkey.backendapi.domain.post.infra.persistence.repository;

import java.util.List;

import org.sopt.pawkey.backendapi.domain.post.domain.repository.PostSelectedCategoryOptionRepository;
import org.sopt.pawkey.backendapi.domain.post.infra.persistence.entity.PostSelectedCategoryOptionEntity;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class PostSelectedCategoryOptionRepositoryImpl implements PostSelectedCategoryOptionRepository {
	private final SpringDataPostSelectedCategoryOptionRepository springDataPostSelectedCategoryOptionRepository;

	@Override
	public void saveBatch(List<PostSelectedCategoryOptionEntity> postSelectedCategoryOptions) {
		springDataPostSelectedCategoryOptionRepository.saveAll(postSelectedCategoryOptions);
	}
}
