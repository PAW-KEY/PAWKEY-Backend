package org.sopt.pawkey.backendapi.domain.dbti.api.dto.response;

import java.util.List;

import org.sopt.pawkey.backendapi.domain.dbti.application.dto.DbtiResultInfo;
import org.sopt.pawkey.backendapi.domain.dbti.domain.model.DbtiType;

public record DbtiResultResponseDto(
	DbtiType type,
	String name,
	String image,
	List<String> keyword,
	String description,
	List<DbtiResultInfoResponse> analysis
) {
	public record DbtiResultInfoResponse(
		String axis, String leftLabel, String rightLabel, String dominantSide, int score
	) {
	}

	public static DbtiResultResponseDto from(DbtiResultInfo info) {
		return new DbtiResultResponseDto(
			DbtiType.valueOf(info.type()),
			info.name(),
			info.imageUrl(),
			info.keywords(),
			info.description(),
			info.analysis().stream()
				.map(a -> new DbtiResultInfoResponse(a.axis(), a.leftLabel(), a.rightLabel(), a.dominantSide(),
					a.score()))
				.toList()
		);
	}
}