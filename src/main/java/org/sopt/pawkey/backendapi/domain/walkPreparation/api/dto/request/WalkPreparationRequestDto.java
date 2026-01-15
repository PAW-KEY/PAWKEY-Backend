package org.sopt.pawkey.backendapi.domain.walkPreparation.api.dto.request;

import java.util.List;

import jakarta.validation.constraints.NotNull;

public record WalkPreparationRequestDto(
	@NotNull(message = "준비물 리스트는 필수입니다.")
	List<String> preparation
) {
}
