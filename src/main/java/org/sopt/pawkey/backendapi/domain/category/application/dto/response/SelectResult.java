package org.sopt.pawkey.backendapi.domain.category.application.dto.response;

import java.util.List;

import org.sopt.pawkey.backendapi.domain.category.infra.persistence.entity.SelectEntity;
import org.sopt.pawkey.backendapi.domain.category.infra.persistence.entity.SelectOptionEntity;

public record SelectResult(
	Long selectId,
	String selectName,
	List<SelectOptionResult> options
) {
	public static SelectResult fromEntity(SelectEntity entity) {
		return new SelectResult(
			entity.getSelectId(),
			entity.getSelectName(),
			entity.getSelectOptionEntityList().stream()
				.map(SelectOptionResult::fromEntity)
				.toList()
		);
	}

	public record SelectOptionResult(
		Long selectOptionId,
		String selectText
	) {
		public static SelectOptionResult fromEntity(SelectOptionEntity entity) {
			return new SelectOptionResult(entity.getId(), entity.getSelectText());
		}
	}
}
