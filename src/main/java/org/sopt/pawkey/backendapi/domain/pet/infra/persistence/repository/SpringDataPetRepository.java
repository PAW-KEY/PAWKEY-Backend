package org.sopt.pawkey.backendapi.domain.pet.infra.persistence.repository;

import java.util.Optional;

import org.sopt.pawkey.backendapi.domain.pet.infra.persistence.entity.PetEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataPetRepository extends JpaRepository<PetEntity, Long> {

	@EntityGraph(attributePaths = {
		"profileImage", "dbtiResult"})
	Optional<PetEntity> findByUser_UserId(Long userId);

	boolean existsByUser_UserId(Long userId);

	@Override
	@EntityGraph(attributePaths = {"profileImage", "dbtiResult"})
	Optional<PetEntity> findById(Long petId);
}
