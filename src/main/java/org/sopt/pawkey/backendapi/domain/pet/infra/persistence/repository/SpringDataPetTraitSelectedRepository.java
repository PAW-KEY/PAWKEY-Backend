package org.sopt.pawkey.backendapi.domain.pet.infra.persistence.repository;

import org.sopt.pawkey.backendapi.domain.pet.infra.persistence.entity.PetTraitOptionEntity;
import org.sopt.pawkey.backendapi.domain.pet.infra.persistence.entity.PetTraitSelectedEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataPetTraitSelectedRepository extends JpaRepository<PetTraitSelectedEntity, Long> {
}



