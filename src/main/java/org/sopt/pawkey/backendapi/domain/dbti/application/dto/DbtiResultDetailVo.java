package org.sopt.pawkey.backendapi.domain.dbti.application.dto;

import org.sopt.pawkey.backendapi.domain.dbti.infra.persistence.entity.DbtiEntity;
import org.sopt.pawkey.backendapi.domain.dbti.infra.persistence.entity.DbtiResultEntity;

public record DbtiResultDetailVo(
	DbtiResultEntity result,
	DbtiEntity dbtiInfo
) {
}
