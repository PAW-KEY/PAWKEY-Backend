package org.sopt.pawkey.backendapi.domain.dbti.domain.repository;

import org.sopt.pawkey.backendapi.domain.dbti.infra.persistence.entity.DbtiResultEntity;

public interface DbtiResultRepository {
	DbtiResultEntity save(DbtiResultEntity result);
}