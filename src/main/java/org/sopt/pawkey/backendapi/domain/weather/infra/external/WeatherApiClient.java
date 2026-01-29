package org.sopt.pawkey.backendapi.domain.weather.infra.external;

import org.sopt.pawkey.backendapi.domain.weather.infra.external.dto.OpenWeatherResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

import org.sopt.pawkey.backendapi.domain.region.infra.persistence.entity.RegionEntity;

@Component
@RequiredArgsConstructor
public class WeatherApiClient {
	private final WeatherClient weatherClient;

	@Value("${weather.api-key}")
	private String apiKey;

	public OpenWeatherResponseDTO fetchWeather(RegionEntity region) {
		return weatherClient.getCurrentWeather(
			region.getLatitude(),
			region.getLongitude(),
			apiKey,
			"metric"
		);
	}
}