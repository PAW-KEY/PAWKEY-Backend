package org.sopt.pawkey.backendapi.domain.walkPreparation.application.facade;

import org.sopt.pawkey.backendapi.domain.walkPreparation.api.dto.request.WalkPreparationRequestDto;
import org.sopt.pawkey.backendapi.domain.walkPreparation.api.dto.response.WalkPreparationResponseDto;
import org.sopt.pawkey.backendapi.domain.walkPreparation.application.service.PreparationService;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PreparationFacade {
	private final PreparationService walkPreparationService;

	public WalkPreparationResponseDto getPreparation(Long userId) {
		return new WalkPreparationResponseDto(walkPreparationService.getPreparationItems(userId));
	}

	public WalkPreparationResponseDto updatePreparation(Long userId, WalkPreparationRequestDto request) {
		return new WalkPreparationResponseDto(
			walkPreparationService.syncPreparationItems(userId, request.preparation()));
	}
}
