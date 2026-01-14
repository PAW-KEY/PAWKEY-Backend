package org.sopt.pawkey.backendapi.domain.weather.application.service;

import org.sopt.pawkey.backendapi.domain.region.infra.persistence.entity.RegionEntity;
import org.sopt.pawkey.backendapi.domain.weather.application.dto.result.WeatherResult;
import org.sopt.pawkey.backendapi.domain.weather.domain.repository.WeatherRepository;
import org.sopt.pawkey.backendapi.domain.weather.infra.cache.WeatherCacheManager;
import org.sopt.pawkey.backendapi.domain.weather.infra.external.WeatherApiClient;
import org.sopt.pawkey.backendapi.domain.weather.infra.external.dto.OpenWeatherResponseDTO;
import org.sopt.pawkey.backendapi.domain.weather.infra.persistence.entity.WeatherEntity;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class WeatherService {

	private final WeatherRepository weatherRepository;
	private final WeatherCacheManager cacheManager;
	private final WeatherApiClient apiClient;

	@Transactional
	public WeatherResult getOrFetchWeather(RegionEntity region) {
		WeatherResult cached = cacheManager.get(region.getRegionId());
		if (cached != null)
			return cached;

		WeatherEntity weather = weatherRepository.findByRegionId(region.getRegionId()).orElse(null);

		if (weather == null) {
			weather = fetchAndSaveNewWeather(region);
		} else if (weather.isStale() || isIncomplete(weather)) {
			fetchAndUpdateWeather(weather, region);
		}

		WeatherResult result = WeatherResult.from(weather);
		cacheManager.save(region.getRegionId(), result, isIncomplete(weather));

		return result;
	}

	private WeatherEntity fetchAndSaveNewWeather(RegionEntity region) {
		OpenWeatherResponseDTO response = apiClient.fetchWeather(region);
		return weatherRepository.save(WeatherEntity.create(
			region.getRegionId(),
			response.getConvertedTemp(),
			response.getConvertedRain() != null ? response.getConvertedRain() : 0,
			response.getConvertedPop() != null ? response.getConvertedPop() : 0,
			response.getWeatherCode()
		));
	}

	private void fetchAndUpdateWeather(WeatherEntity weather, RegionEntity region) {
		try {
			OpenWeatherResponseDTO response = apiClient.fetchWeather(region);
			weather.updateWeather(
				response.getConvertedTemp(),
				response.getConvertedRain() != null ? response.getConvertedRain() : 0,
				response.getConvertedPop() != null ? response.getConvertedPop() : 0,
				response.getWeatherCode()
			);
		} catch (Exception e) {
			log.warn("날씨 업데이트 실패: {}", e.getMessage());
		}
	}

	private boolean isIncomplete(WeatherEntity weather) {
		return weather.getTemperature() == null || weather.getRainyMm() == null || weather.getRainyProb() == null;
	}
}