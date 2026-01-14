package org.sopt.pawkey.backendapi.domain.weather.api.dto;

import lombok.Builder;

@Builder
public record RegionWeatherResponse(
	Integer temperature,
	Integer rainyMm,
	String region
) {
	public static RegionWeatherResponse of(Integer temperature, Integer rainyMm, String regionName) {
		return RegionWeatherResponse.builder()
			.temperature(temperature)
			.rainyMm(rainyMm)
			.region(regionName)
			.build();
	}
}