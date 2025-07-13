package org.sopt.pawkey.backendapi.domain.category.api.dto.response;

import java.util.List;

import org.sopt.pawkey.backendapi.domain.category.application.dto.result.CategoryResult;

public record CategoryResponseDto (
	Long categoryId,
	String categoryName,
	List<CategoryOptionResponseDto> categoryOptions

){
	public static CategoryResponseDto from(CategoryResult categoryResult){
		return new CategoryResponseDto(
			categoryResult.categoryId(),
			categoryResult.categoryName(),
			categoryResult.options().stream()
				.map(CategoryOptionResponseDto::from)
				.toList()
		);
	}

	public record CategoryOptionResponseDto(
		Long categoryOptionId,
		String categoryOptionText
	){
		public static CategoryOptionResponseDto from(CategoryResult.CategoryOptionResult categoryOptionResult){
			return new CategoryOptionResponseDto(
					categoryOptionResult.categoryOptionId(),
					categoryOptionResult.optionText()
			);
		}
	}
}
