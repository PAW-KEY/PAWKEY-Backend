package org.sopt.pawkey.backendapi.domain.dbti.infra.persistence.repository;

import org.sopt.pawkey.backendapi.domain.dbti.domain.model.DbtiType;
import org.sopt.pawkey.backendapi.domain.dbti.infra.persistence.entity.DbtiEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataDbtiRepository extends JpaRepository<DbtiEntity, DbtiType> {
}