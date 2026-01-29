package org.sopt.pawkey.backendapi.domain.dbti.infra.persistence.repository;

import org.sopt.pawkey.backendapi.domain.dbti.infra.persistence.entity.DbtiOptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataDbtiOptionRepository extends JpaRepository<DbtiOptionEntity, Long> {
}