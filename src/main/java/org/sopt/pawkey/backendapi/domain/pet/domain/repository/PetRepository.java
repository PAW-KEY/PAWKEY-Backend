package org.sopt.pawkey.backendapi.domain.pet.domain.repository;

import java.util.Optional;

import org.sopt.pawkey.backendapi.domain.pet.infra.persistence.entity.PetEntity;

public interface PetRepository {

	PetEntity save(PetEntity pet);

	Optional<PetEntity> findByUserId(Long userId);

	Optional<PetEntity> findById(Long petId);

	boolean existsByUserId(Long petId);
}
