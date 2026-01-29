package org.sopt.pawkey.backendapi.domain.pet.infra.persistence.repository;

import java.util.List;

import org.sopt.pawkey.backendapi.domain.pet.infra.persistence.entity.BreedEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataBreedRepository extends JpaRepository<BreedEntity, Long> {
	List<BreedEntity> findAllByOrderByNameAsc();
}
