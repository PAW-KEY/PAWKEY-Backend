package org.sopt.pawkey.backendapi.domain.category.application.dto.result;

import java.util.List;

import org.sopt.pawkey.backendapi.domain.category.domain.model.CategorySelectionType;
import org.sopt.pawkey.backendapi.domain.category.infra.persistence.entity.CategoryEntity;
import org.sopt.pawkey.backendapi.domain.category.infra.persistence.entity.CategoryOptionEntity;
import org.sopt.pawkey.backendapi.domain.category.infra.persistence.entity.DurationEntity;
import org.sopt.pawkey.backendapi.domain.category.infra.persistence.entity.DurationOptionEntity;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record CategoryResult(
	Long id,
	String name,
	CategorySelectionType selectionType,
	List<CategoryOptionResult> options

) {
	//상위 -  카테고리(상세 옵션 포함)
	public static CategoryResult fromCategory(CategoryEntity categoryEntity) {
		return new CategoryResult(
			categoryEntity.getCategoryId(),
			categoryEntity.getCategoryName(),
			categoryEntity.getSelectionType(),
			categoryEntity.getOptions().stream()
				.map(CategoryOptionResult::fromCategoryOption)
				.toList()
		);
	}

	public static CategoryResult fromDuration(DurationEntity entity) {
		return new CategoryResult(
			entity.getDurationId(),
			entity.getDurationName(),
			entity.getSelectionType(),
			entity.getOptions().stream()
				.map(CategoryOptionResult::fromDurationOption)
				.toList()
		);
	}

	//하위 -  카테고리 별 상세 옵션
	public record CategoryOptionResult(
		Long id,
		String text
	) {
		public static CategoryOptionResult fromCategoryOption(CategoryOptionEntity entity) {
			return new CategoryOptionResult(entity.getId(), entity.getOptionValue());
		}

		public static CategoryOptionResult fromDurationOption(DurationOptionEntity entity) {
			return new CategoryOptionResult(entity.getId(), entity.getDurationText());
		}
	}
}
