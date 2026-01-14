package org.sopt.pawkey.backendapi.domain.dbti.domain.repository;

import java.util.Optional;

import org.sopt.pawkey.backendapi.domain.dbti.infra.persistence.entity.DbtiResultEntity;

public interface DbtiResultRepository {
	Optional<DbtiResultEntity> findByPetId(Long petId);

	DbtiResultEntity save(DbtiResultEntity result);
}