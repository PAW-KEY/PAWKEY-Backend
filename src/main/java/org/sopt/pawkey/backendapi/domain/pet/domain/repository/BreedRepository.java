package org.sopt.pawkey.backendapi.domain.pet.domain.repository;

import java.util.List;
import java.util.Optional;

import org.sopt.pawkey.backendapi.domain.pet.infra.persistence.entity.BreedEntity;

public interface BreedRepository {
	List<BreedEntity> findAll();

	Optional<BreedEntity> findById(Long id);
}

