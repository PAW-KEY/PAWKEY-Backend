package org.sopt.pawkey.backendapi.domain.walkPreparation.domain.repository;

import java.util.Optional;

import org.sopt.pawkey.backendapi.domain.walkPreparation.infra.persistence.entity.PreparationEntity;

public interface PreparationRepository {
	Optional<PreparationEntity> findByUserId(Long userId);

	PreparationEntity save(PreparationEntity preparationEntity);
}
