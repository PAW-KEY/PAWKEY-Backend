package org.sopt.pawkey.backendapi.domain.weather.api.dto;

import lombok.Builder;

@Builder
public record RegionWeatherResponseDTO(
	Integer temperature,
	Integer rainyMm,
	String region
) {
	public static RegionWeatherResponseDTO of(Integer temperature, Integer rainyMm, String regionName) {
		return RegionWeatherResponseDTO.builder()
			.temperature(temperature)
			.rainyMm(rainyMm)
			.region(regionName)
			.build();
	}
}