package org.sopt.pawkey.backendapi.domain.pet.infra.persistence.repository;

import org.sopt.pawkey.backendapi.domain.pet.infra.persistence.entity.PetEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataPetRepository extends JpaRepository<PetEntity, Long> {
}
