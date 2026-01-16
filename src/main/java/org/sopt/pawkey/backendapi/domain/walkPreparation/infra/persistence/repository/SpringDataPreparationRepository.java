package org.sopt.pawkey.backendapi.domain.walkPreparation.infra.persistence.repository;

import java.util.Optional;

import org.sopt.pawkey.backendapi.domain.walkPreparation.infra.persistence.entity.PreparationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataPreparationRepository extends JpaRepository<PreparationEntity, Long> {
	Optional<PreparationEntity> findByUserId(Long userId);
}
