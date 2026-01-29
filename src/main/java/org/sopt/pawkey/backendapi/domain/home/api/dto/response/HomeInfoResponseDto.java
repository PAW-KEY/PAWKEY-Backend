package org.sopt.pawkey.backendapi.domain.home.api.dto.response;

public record HomeInfoResponseDto(
	Double distance,
	Integer time,
	Integer count
) {
	public static HomeInfoResponseDto of(Double distance, Integer time, Long count) {
		return new HomeInfoResponseDto(
			distance != null ? Math.round(distance * 10) / 10.0 : 0.0,
			time != null ? time : 0,
			count != null ? count.intValue() : 0
		);
	}
}
