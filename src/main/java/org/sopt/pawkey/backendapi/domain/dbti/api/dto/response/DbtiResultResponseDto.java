package org.sopt.pawkey.backendapi.domain.dbti.api.dto.response;

import java.util.List;

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
	public static DbtiResultResponseDto of(DbtiResultEntity result, String name, String image, List<String> keywords,
		String description) {
		return new DbtiResultResponseDto(
			result.getFinalType(),
			name,
			image,
			keywords,
			description,
			result.getEiScore(),
			result.getPsScore(),
			result.getRtScore()
		);
	}
}