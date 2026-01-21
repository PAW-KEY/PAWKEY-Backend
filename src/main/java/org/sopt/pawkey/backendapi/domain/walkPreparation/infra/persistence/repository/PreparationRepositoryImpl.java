package org.sopt.pawkey.backendapi.domain.walkPreparation.infra.persistence.repository;

import java.util.Optional;

import org.sopt.pawkey.backendapi.domain.walkPreparation.domain.repository.PreparationRepository;
import org.sopt.pawkey.backendapi.domain.walkPreparation.infra.persistence.entity.PreparationEntity;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class PreparationRepositoryImpl implements PreparationRepository {
	private final SpringDataPreparationRepository springDataPreparationRepository;

	@Override
	public Optional<PreparationEntity> findByUserId(Long userId) {
		return springDataPreparationRepository.findByUserId(userId);
	}

	@Override
	public PreparationEntity save(PreparationEntity preparationEntity) {
		return springDataPreparationRepository.save(preparationEntity);
	}
}
