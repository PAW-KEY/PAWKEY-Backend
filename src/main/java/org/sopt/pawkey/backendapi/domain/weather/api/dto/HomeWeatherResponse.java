package org.sopt.pawkey.backendapi.domain.weather.api.dto;

import lombok.Builder;

@Builder
public record HomeWeatherResponse(
	Integer temperature,
	Integer rainyMm,
	String region
) {
	public static HomeWeatherResponse of(Integer temperature, Integer rainyMm, String regionName) {
		return HomeWeatherResponse.builder()
			.temperature(temperature)
			.rainyMm(rainyMm)
			.region(regionName)
			.build();
	}
}