package org.sopt.pawkey.backendapi.domain.category.api.dto.response;

import java.util.List;

import org.sopt.pawkey.backendapi.domain.category.application.dto.result.CategoryResult;

public record CategoryListResponseDto (
	List <CategoryResponseDto> categoryList)
{
	public static CategoryListResponseDto from(List<CategoryResult> results) {
		return new CategoryListResponseDto(
			results.stream()
				.map(CategoryResponseDto::from)
				.toList()
		);
	}
}



// public static PetTraitCategoryListResponseDto from(List<PetTraitCategoryResult> results) {
// 	return new PetTraitCategoryListResponseDto(
// 		results.stream()
// 			.map(PetTraitCategoryResponseDto::from)
// 			.toList()
// 	);