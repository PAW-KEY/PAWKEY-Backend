package org.sopt.pawkey.backendapi.domain.dbti.infra.persistence.repository;

import org.sopt.pawkey.backendapi.domain.dbti.infra.persistence.entity.DbtiTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataDbtiTypeRepository extends JpaRepository<DbtiTypeEntity, Long> {
}