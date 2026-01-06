package org.sopt.pawkey.backendapi.domain.weather.application.service;

import org.sopt.pawkey.backendapi.domain.region.infra.persistence.entity.RegionEntity;
import org.sopt.pawkey.backendapi.domain.weather.domain.repository.WeatherRepository;
import org.sopt.pawkey.backendapi.domain.weather.infra.persistence.entity.WeatherEntity;
import org.sopt.pawkey.backendapi.global.infra.external.weather.WeatherClient;
import org.sopt.pawkey.backendapi.global.infra.external.weather.dto.WeatherResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WeatherService {

	private final WeatherRepository weatherRepository;
	private final WeatherClient weatherClient;

	@Value("${weather.api-key}")
	private String apiKey;

	@Transactional
	public WeatherEntity getOrFetchWeather(RegionEntity region) {
		WeatherEntity weather = weatherRepository.findByRegionId(region.getRegionId())
			.orElseGet(() -> createNewWeather(region.getRegionId()));

		// 1시간 지났는지 확인
		if (weather.isStale() || weather.getTemperature() == null) {
			WeatherResponse response = weatherClient.getCurrentWeather(
				region.getLatitude(),
				region.getLongitude(),
				apiKey,
				"metric"
			);

			weather.updateWeather(
				response.getConvertedTemp(),
				response.getConvertedRain(),
				response.getConvertedPop(),
				response.getWeatherCode()
			);
		}
		return weather;
	}

	// 처음 해당 지역 날씨를 조회할 때 빈 엔티티를 생성
	private WeatherEntity createNewWeather(Long regionId) {
		WeatherEntity newWeather = WeatherEntity.builder()
			.regionId(regionId)
			.build();
		return weatherRepository.save(newWeather);
	}
}
