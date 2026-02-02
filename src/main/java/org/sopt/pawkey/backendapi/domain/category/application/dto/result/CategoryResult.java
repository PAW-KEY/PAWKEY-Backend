package org.sopt.pawkey.backendapi.domain.category.application.dto.result;

import java.util.List;

import org.sopt.pawkey.backendapi.domain.category.infra.persistence.entity.CategoryEntity;
import org.sopt.pawkey.backendapi.domain.category.infra.persistence.entity.CategoryOptionEntity;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record CategoryResult(
	Long categoryId,
	String categoryName,
	List<CategoryOptionResult> options

) {
	//상위 -  카테고리(상세 옵션 포함)
	public static CategoryResult fromEntity(CategoryEntity categoryEntity) {
		return new CategoryResult(
			categoryEntity.getCategoryId(),
			categoryEntity.getCategoryName(),
			categoryEntity.getOptions().stream()
				.map(CategoryOptionResult::fromEntity)
				.toList()
		);
	}

	public static CategoryResult fromEntityWithSummary(CategoryEntity categoryEntity) {
		return new CategoryResult(
			categoryEntity.getCategoryId(),
			categoryEntity.getCategoryName(),
			categoryEntity.getOptions().stream()
				.map(CategoryOptionResult::fromEntity)
				.toList()
		);
	}

	//하위 -  카테고리 별 상세 옵션
	public record CategoryOptionResult(
		Long categoryOptionId,
		String optionValue
	) {
		public static CategoryOptionResult fromEntity(CategoryOptionEntity categoryOptionEntity) {
			return new CategoryOptionResult(categoryOptionEntity.getId(), categoryOptionEntity.getOptionValue());
		}
	}
}

