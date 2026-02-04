package org.sopt.pawkey.backendapi.domain.dbti.application.dto;

import java.util.List;

import org.sopt.pawkey.backendapi.domain.dbti.infra.persistence.entity.DbtiEntity;
import org.sopt.pawkey.backendapi.domain.dbti.infra.persistence.entity.DbtiResultEntity;
import org.sopt.pawkey.backendapi.domain.dbti.infra.persistence.entity.DbtiTypeEntity;

public record DbtiResultInfo(
	String type,
	String name,
	String imageUrl,
	List<String> keywords,
	String description,
	List<AnalysisDetail> analysis
) {
	public record AnalysisDetail(
		String axis,
		String leftLabel,
		String rightLabel,
		String dominantSide,
		int score
	) {
	}

	public static DbtiResultInfo from(DbtiResultEntity result, DbtiEntity dbtiInfo, List<DbtiTypeEntity> types) {
		List<AnalysisDetail> analysis = types.stream()
			.map(type -> {
				var res = result.getAnalysisOf(type.getCode());
				return new AnalysisDetail(
					type.getCode(), type.getLeftLabel(), type.getRightLabel(), res.side(), res.score()
				);
			}).toList();

		return new DbtiResultInfo(
			result.getDbtiType().name(),
			dbtiInfo.getName(),
			dbtiInfo.getImageUrl(),
			java.util.Arrays.asList(dbtiInfo.getKeywords().split(",")),
			dbtiInfo.getDescription(),
			analysis
		);
	}
}

