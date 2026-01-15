package org.sopt.pawkey.backendapi.domain.walkPreparation.application.service;

import java.util.List;

import org.sopt.pawkey.backendapi.domain.walkPreparation.domain.repository.PreparationRepository;
import org.sopt.pawkey.backendapi.domain.walkPreparation.infra.persistence.entity.PreparationEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class PreparationService {
	private final PreparationRepository preparationRepository;

	@Transactional(readOnly = true)
	public List<String> getPreparationItems(Long userId) {
		return preparationRepository.findByUserId(userId)
			.map(PreparationEntity::getItems)
			.orElse(List.of("배변 봉투", "리드 줄")); // 기본값
	}

	public List<String> syncPreparationItems(Long userId, List<String> newItems) {
		PreparationEntity entity = preparationRepository.findByUserId(userId)
			.orElseGet(() -> PreparationEntity.builder().userId(userId).build());

		entity.updateItems(newItems);
		return preparationRepository.save(entity).getItems();
	}
}
