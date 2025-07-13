package org.sopt.pawkey.backendapi.domain.pet.application.dto.response;

import java.util.List;

import org.sopt.pawkey.backendapi.domain.pet.infra.persistence.entity.PetTraitCategoryEntity;
import org.sopt.pawkey.backendapi.domain.pet.infra.persistence.entity.PetTraitOptionEntity;

public record PetTraitCategoryResult(
	Long id,
	String name,
	List<PetTraitOptionResult> options
) {


	//상위 - 강아지 특성 카테고리(상세 옵션 포함)
	public static PetTraitCategoryResult fromEntity(PetTraitCategoryEntity petTraitCategory) {
		return new PetTraitCategoryResult(
			petTraitCategory.getId(),
			petTraitCategory.getCategoryName(),
			petTraitCategory.getPetTraitOptionEntityList().stream()
				.map(PetTraitOptionResult::fromEntity)
				.toList()
		);
	}
	//하위 - 강아지 카테고리 별 상세 옵션
	public record PetTraitOptionResult(
		Long id,
		String text
	) {
		public static PetTraitOptionResult fromEntity(PetTraitOptionEntity entity) {
			return new PetTraitOptionResult(entity.getId(), entity.getOptionText());
		}
	}
}
