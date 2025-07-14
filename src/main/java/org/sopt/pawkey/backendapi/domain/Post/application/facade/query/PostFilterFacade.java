package org.sopt.pawkey.backendapi.domain.post.application.facade.query;

import java.util.List;

import org.sopt.pawkey.backendapi.domain.category.api.dto.response.CategoryListResponseDto;
import org.sopt.pawkey.backendapi.domain.category.application.dto.response.CategoryResult;
import org.sopt.pawkey.backendapi.domain.category.application.service.CategoryQueryService;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PostFilterFacade {
	private final CategoryQueryService categoryQueryService;

	public CategoryListResponseDto getAllCategories() {
		List<CategoryResult> results = categoryQueryService.getAllCategoriesSummary();
		return CategoryListResponseDto.from(results);
	}
}
