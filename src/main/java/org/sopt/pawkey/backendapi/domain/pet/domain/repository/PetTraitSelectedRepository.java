package org.sopt.pawkey.backendapi.domain.pet.domain.repository;

import org.sopt.pawkey.backendapi.domain.pet.infra.persistence.entity.PetTraitSelectedEntity;

public interface PetTraitSelectedRepository {
	void save(PetTraitSelectedEntity entity);
}
