package org.sopt.pawkey.backendapi.domain.homeWeather.api.dto;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class")
public record HomeInfoResponseDto(
	Double distance,
	Integer totalTime,
	Integer count
) {
	public HomeInfoResponseDto {
		distance = distance != null ? distance : 0.0;
		totalTime = totalTime != null ? totalTime : 0;
		count = count != null ? count : 0;
	}
}