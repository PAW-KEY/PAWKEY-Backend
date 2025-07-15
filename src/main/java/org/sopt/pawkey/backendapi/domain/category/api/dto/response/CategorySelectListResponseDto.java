package org.sopt.pawkey.backendapi.domain.category.api.dto.response;

import java.util.List;

import org.sopt.pawkey.backendapi.domain.category.application.dto.response.SelectResult;
import org.sopt.pawkey.backendapi.domain.category.application.dto.result.CategoryResult;

public record CategorySelectListResponseDto(
	List<SelectResult> selectList,
	List<CategoryResult> categoryList
) {
	public static CategorySelectListResponseDto of(List<SelectResult> selects, List<CategoryResult> categories) {
		return new CategorySelectListResponseDto(selects, categories);
	}
}
