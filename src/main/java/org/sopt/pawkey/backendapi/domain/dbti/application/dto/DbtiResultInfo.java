package org.sopt.pawkey.backendapi.domain.dbti.application.dto;

import java.util.List;

import org.sopt.pawkey.backendapi.domain.dbti.infra.persistence.entity.DbtiEntity;
import org.sopt.pawkey.backendapi.domain.dbti.infra.persistence.entity.DbtiResultEntity;
import org.sopt.pawkey.backendapi.domain.dbti.infra.persistence.entity.DbtiTypeEntity;

public record DbtiResultInfo(
	DbtiResultEntity result,
	DbtiEntity dbtiInfo,
	List<DbtiTypeEntity> types
) {
}
