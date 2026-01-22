package org.sopt.pawkey.backendapi.domain.pet.infra.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.sopt.pawkey.backendapi.domain.pet.infra.persistence.entity.PetEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataPetRepository extends JpaRepository<PetEntity, Long> {

	@EntityGraph(attributePaths = {
		"profileImage", "dbtiResult"})
	List<PetEntity> findAllByUser_UserId(Long userId);

	@Override
	@EntityGraph(attributePaths = {"profileImage", "dbtiResult"})
	Optional<PetEntity> findById(Long petId);
}
