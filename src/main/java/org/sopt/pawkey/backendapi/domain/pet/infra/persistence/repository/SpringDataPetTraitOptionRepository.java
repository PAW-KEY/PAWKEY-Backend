package org.sopt.pawkey.backendapi.domain.pet.infra.persistence.repository;

import org.sopt.pawkey.backendapi.domain.pet.infra.persistence.entity.PetTraitCategoryEntity;
import org.sopt.pawkey.backendapi.domain.pet.infra.persistence.entity.PetTraitOptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataPetTraitOptionRepository  extends JpaRepository<PetTraitOptionEntity, Long> {
}
