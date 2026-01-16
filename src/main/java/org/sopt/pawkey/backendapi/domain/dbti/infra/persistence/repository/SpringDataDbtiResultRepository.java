package org.sopt.pawkey.backendapi.domain.dbti.infra.persistence.repository;

import java.util.Optional;

import org.sopt.pawkey.backendapi.domain.dbti.infra.persistence.entity.DbtiResultEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataDbtiResultRepository extends JpaRepository<DbtiResultEntity, Long> {
	Optional<DbtiResultEntity> findByPetId(Long petId);
}