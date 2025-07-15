package org.sopt.pawkey.backendapi.domain.post.application.service;

import java.util.List;

import org.sopt.pawkey.backendapi.domain.category.infra.persistence.entity.CategoryOptionEntity;
import org.sopt.pawkey.backendapi.domain.post.domain.repository.PostSelectedCategoryOptionRepository;
import org.sopt.pawkey.backendapi.domain.post.infra.persistence.entity.PostEntity;
import org.sopt.pawkey.backendapi.domain.post.infra.persistence.entity.PostSelectedCategoryOptionEntity;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostSelectedCategoryOptionService {
	private final PostSelectedCategoryOptionRepository postSelectedCategoryOptionRepository;

	public void saveSelectedOption(PostEntity post, List<CategoryOptionEntity> selectedOptions) {

		List<PostSelectedCategoryOptionEntity> postSelectedCategoryOptions = selectedOptions.stream()
			.map(selectedOption -> PostSelectedCategoryOptionEntity
				.builder()
				.post(post)
				.categoryOption(selectedOption)
				.build())
			.toList();

		postSelectedCategoryOptionRepository.saveBatch(postSelectedCategoryOptions);
	}
}
