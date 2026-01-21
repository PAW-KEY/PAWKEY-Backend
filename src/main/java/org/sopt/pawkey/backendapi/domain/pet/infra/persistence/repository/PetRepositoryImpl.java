package org.sopt.pawkey.backendapi.domain.pet.infra.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.sopt.pawkey.backendapi.domain.pet.domain.repository.PetRepository;
import org.sopt.pawkey.backendapi.domain.pet.infra.persistence.entity.BreedEntity;
import org.sopt.pawkey.backendapi.domain.pet.infra.persistence.entity.PetEntity;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class PetRepositoryImpl implements PetRepository {

	private final SpringDataPetRepository springDataPetRepository;
	private final SpringDataBreedRepository springDataBreedRepository;

	@Override
	public PetEntity save(PetEntity pet) {
		return springDataPetRepository.save(pet);
	}

	@Override
	public List<PetEntity> findAllPetsByUserId(Long userId) {
		return springDataPetRepository.findAllByUser_UserId(userId);
	}

	@Override
	public boolean existsById(Long petId) {
		return springDataPetRepository.existsById(petId);
	}

	@Override
	public List<String> findAllBreeds() {
		return springDataBreedRepository.findAllByOrderByNameAsc()
			.stream()
			.map(BreedEntity::getName)
			.sorted()
			.toList();
	}

	@Override
	public Optional<PetEntity> findById(Long petId) {
		return springDataPetRepository.findById(petId);
	}
}
