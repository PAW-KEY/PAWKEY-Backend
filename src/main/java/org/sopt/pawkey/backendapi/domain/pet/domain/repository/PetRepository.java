package org.sopt.pawkey.backendapi.domain.pet.domain.repository;

import java.util.List;
import java.util.Optional;

import org.sopt.pawkey.backendapi.domain.pet.infra.persistence.entity.PetEntity;

public interface PetRepository {

	PetEntity save(PetEntity pet);

	List<PetEntity> findAllPetsByUserId(Long userId);

	Optional<PetEntity> findById(Long petId);
  
	boolean existsById(Long petId);

	List<String> findAllBreeds();
}
