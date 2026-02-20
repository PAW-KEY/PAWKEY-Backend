package org.sopt.pawkey.backendapi.domain.category.api.dto.response;

import java.util.List;

import org.sopt.pawkey.backendapi.domain.category.application.dto.result.CategoryResult;

public record PostCategoryListResponseDto(
	List<CategoryResult> durationList,
	List<CategoryResult> categoryList
) {
	public static PostCategoryListResponseDto of(List<CategoryResult> durations, List<CategoryResult> categories) {
		return new PostCategoryListResponseDto(durations, categories);
	}
}