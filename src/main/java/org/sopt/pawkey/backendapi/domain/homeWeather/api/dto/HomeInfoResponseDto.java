package org.sopt.pawkey.backendapi.domain.homeWeather.api.dto;

public record HomeInfoResponseDto(
	Double distance,
	Integer totalTime,
	Integer count
) {
	public HomeInfoResponseDto {
		distance = distance != null ? Math.round((distance / 1000.0) * 10) / 10.0 : 0.0;

		totalTime = totalTime != null ? totalTime : 0;
		count = count != null ? count : 0;
	}

	public static HomeInfoResponseDto of(Double distance, Integer totalTime, Integer count) {
		return new HomeInfoResponseDto(distance, totalTime, count);
	}
}