package org.sopt.pawkey.backendapi.domain.dbti.infra.persistence.repository;

import java.util.Optional;

import org.sopt.pawkey.backendapi.domain.dbti.domain.repository.DbtiResultRepository;
import org.sopt.pawkey.backendapi.domain.dbti.infra.persistence.entity.DbtiResultEntity;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class DbtiResultRepositoryImpl implements DbtiResultRepository {
	private final SpringDataDbtiResultRepository springDataDbtiResultRepository;

	@Override
	public DbtiResultEntity save(DbtiResultEntity result) {
		return springDataDbtiResultRepository.save(result);
	}

	@Override
	public void deleteByUserId(Long userId) {
		springDataDbtiResultRepository.deleteByUserId(userId);

	}

	@Override
	public Optional<DbtiResultEntity> findByPetId(Long petId) {
		return springDataDbtiResultRepository.findByPet_PetId(petId);
	}
}