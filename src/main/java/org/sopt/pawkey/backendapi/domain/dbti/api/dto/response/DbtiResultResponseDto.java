package org.sopt.pawkey.backendapi.domain.dbti.api.dto.response;

import java.util.Arrays;
import java.util.List;

import org.sopt.pawkey.backendapi.domain.dbti.infra.persistence.entity.DbtiEntity;
import org.sopt.pawkey.backendapi.domain.dbti.infra.persistence.entity.DbtiResultEntity;

public record DbtiResultResponseDto(
	String type,
	String name,
	String image,
	List<String> keyword,
	String description,
	int energy,
	int sociality,
	int routine
) {
	public static DbtiResultResponseDto of(DbtiResultEntity result, DbtiEntity dbtiInfo) {
		return new DbtiResultResponseDto(
			result.getDbtiType(),
			dbtiInfo.getName(),
			dbtiInfo.getImageUrl(),
			Arrays.asList(dbtiInfo.getKeywords().split(",")),
			dbtiInfo.getDescription(),
			result.getEiScore(),
			result.getPsScore(),
			result.getRfScore()
		);
	}
}