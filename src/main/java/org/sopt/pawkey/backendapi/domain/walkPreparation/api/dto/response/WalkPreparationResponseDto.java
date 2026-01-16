package org.sopt.pawkey.backendapi.domain.walkPreparation.api.dto.response;

import java.util.List;

public record WalkPreparationResponseDto(
	List<String> preparation
) {
	public static WalkPreparationResponseDto from(List<String> preparation) {
		return new WalkPreparationResponseDto(preparation);
	}
}
