package org.sopt.pawkey.backendapi.domain.category.application.facade.query;

import java.util.List;

import org.sopt.pawkey.backendapi.domain.category.api.dto.response.CategorySelectListResponseDto;
import org.sopt.pawkey.backendapi.domain.category.application.dto.response.SelectResult;
import org.sopt.pawkey.backendapi.domain.category.application.dto.result.CategoryResult;
import org.sopt.pawkey.backendapi.domain.category.application.service.CategoryQueryService;
import org.sopt.pawkey.backendapi.domain.category.application.service.SelectQueryService;
import org.springframework.stereotype.Component;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@Transactional
public class CategorySelectFacade {
	private final CategoryQueryService categoryQueryService;
	private final SelectQueryService selectQueryService;

	public CategorySelectListResponseDto getFilterOptions() {
		List<SelectResult> selects = selectQueryService.getAllSelects();
		List<CategoryResult> categories = categoryQueryService.getAllCategoriesSummary();

		return CategorySelectListResponseDto.of(selects, categories);
	}
}
