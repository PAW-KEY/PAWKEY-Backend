package org.sopt.pawkey.backendapi.domain.category.application.dto.result;

import java.util.List;

import org.sopt.pawkey.backendapi.domain.category.infra.persistence.entity.CategoryEntity;
import org.sopt.pawkey.backendapi.domain.category.infra.persistence.entity.CategoryOptionEntity;

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
			categoryEntity.getCategoryOptionEntityList().stream()
				.map(CategoryOptionResult::fromEntity)
				.toList()
		);
	}

	public static CategoryResult fromEntityWithSummary(CategoryEntity categoryEntity) {
		return new CategoryResult(
			categoryEntity.getCategoryId(),
			categoryEntity.getCategoryName(),
			categoryEntity.getCategoryOptionEntityList().stream()
				.map(CategoryOptionResult::fromEntityWithSummary)
				.toList()
		);
	}

	//하위 -  카테고리 별 상세 옵션
	public record CategoryOptionResult(
		Long categoryOptionId,
		String optionText
	) {
		public static CategoryOptionResult fromEntity(CategoryOptionEntity categoryOptionEntity) {
			return new CategoryOptionResult(categoryOptionEntity.getId(), categoryOptionEntity.getOptionText());
		}

		public static CategoryOptionResult fromEntityWithSummary(CategoryOptionEntity entity) {
			return new CategoryOptionResult(
				entity.getId(),
				entity.getOptionSummary()
			);
		}
	}
}

