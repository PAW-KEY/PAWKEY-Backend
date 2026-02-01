package org.sopt.pawkey.backendapi.domain.dbti.api.dto.response;

import java.util.Arrays;
import java.util.List;

import org.sopt.pawkey.backendapi.domain.dbti.domain.model.DbtiType;
import org.sopt.pawkey.backendapi.domain.dbti.infra.persistence.entity.DbtiEntity;
import org.sopt.pawkey.backendapi.domain.dbti.infra.persistence.entity.DbtiResultEntity;
import org.sopt.pawkey.backendapi.domain.dbti.infra.persistence.entity.DbtiTypeEntity;

public record DbtiResultResponseDto(
	DbtiType type,
	String name,
	String image,
	List<String> keyword,
	String description,
	List<DbtiResultInfo> analysis
) {
	public record DbtiResultInfo(
		String axis,
		String leftLabel,
		String rightLabel,
		String dominantSide,
		int score
	) {
	}

	public static DbtiResultResponseDto of(
		DbtiResultEntity result,
		DbtiEntity dbtiInfo,
		List<DbtiTypeEntity> types
	) {
		return new DbtiResultResponseDto(
			result.getDbtiType(),
			dbtiInfo.getName(),
			dbtiInfo.getImageUrl(),
			Arrays.asList(dbtiInfo.getKeywords().split(",")),
			dbtiInfo.getDescription(),
			types.stream()
				.map(type -> {
					var analysisResult = result.getAnalysisOf(type.getCode());

					return new DbtiResultInfo(
						type.getCode(),
						type.getLeftLabel(),
						type.getRightLabel(),
						analysisResult.side(),
						analysisResult.score()
					);
				})
				.toList()
		);
	}
}