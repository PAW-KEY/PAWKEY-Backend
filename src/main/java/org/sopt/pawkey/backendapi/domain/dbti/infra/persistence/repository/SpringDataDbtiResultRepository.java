package org.sopt.pawkey.backendapi.domain.dbti.infra.persistence.repository;

import org.sopt.pawkey.backendapi.domain.dbti.infra.persistence.entity.DbtiResultEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataDbtiResultRepository extends JpaRepository<DbtiResultEntity, Long> {
}