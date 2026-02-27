package org.sopt.pawkey.backendapi.domain.dbti.application.dto;

import java.util.List;

import org.sopt.pawkey.backendapi.domain.dbti.exception.DbtiBusinessException;
import org.sopt.pawkey.backendapi.domain.dbti.exception.DbtiErrorCode;
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

	public static DbtiResultInfo of(
		DbtiResultEntity result,
		DbtiEntity dbtiInfo,
		List<DbtiTypeEntity> types
	) {
		List<String> targetOrder = List.of("EI", "PS", "RF");

		List<AnalysisDetail> analysis = targetOrder.stream()
			.map(code -> {
				DbtiTypeEntity type = types.stream()
					.filter(t -> t.getCode().equals(code))
					.findFirst()
					.orElseThrow(() -> new DbtiBusinessException(DbtiErrorCode.DBTI_TYPE_NOT_FOUND));

				var res = result.getAnalysisOf(type.getCode());
				return new AnalysisDetail(
					type.getCode(),
					type.getLeftLabel(),
					type.getRightLabel(),
					res.side(),
					res.score()
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