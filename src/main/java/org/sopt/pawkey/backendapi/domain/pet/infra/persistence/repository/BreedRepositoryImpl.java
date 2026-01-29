package org.sopt.pawkey.backendapi.domain.pet.infra.persistence.repository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.sopt.pawkey.backendapi.domain.pet.domain.repository.BreedRepository;
import org.sopt.pawkey.backendapi.domain.pet.infra.persistence.entity.BreedEntity;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class BreedRepositoryImpl implements BreedRepository {

	private final SpringDataBreedRepository springDataBreedRepository;

	@Override
	public List<BreedEntity> findAll() {
		return springDataBreedRepository.findAllByOrderByNameAsc()
			.stream()
			.sorted(Comparator.comparing(BreedEntity::getName))
			.toList();
	}

	@Override
	public Optional<BreedEntity> findById(Long id) {
		return springDataBreedRepository.findById(id);
	}
}