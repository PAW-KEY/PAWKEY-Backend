package org.sopt.pawkey.backendapi.domain.homeWeather.application.service;

import org.sopt.pawkey.backendapi.domain.region.infra.persistence.entity.RegionEntity;
import org.sopt.pawkey.backendapi.domain.homeWeather.application.dto.result.WeatherMessageResult;
import org.sopt.pawkey.backendapi.domain.homeWeather.application.dto.result.WeatherResult;
import org.sopt.pawkey.backendapi.domain.homeWeather.domain.model.WeatherMessage;
import org.sopt.pawkey.backendapi.domain.homeWeather.domain.repository.WeatherRepository;
import org.sopt.pawkey.backendapi.domain.homeWeather.domain.service.WeatherCommentaryGenerator;
import org.sopt.pawkey.backendapi.domain.homeWeather.infra.cache.WeatherCacheManager;
import org.sopt.pawkey.backendapi.domain.homeWeather.infra.external.WeatherApiClient;
import org.sopt.pawkey.backendapi.domain.homeWeather.infra.external.dto.OpenWeatherResponseDTO;
import org.sopt.pawkey.backendapi.domain.homeWeather.infra.persistence.entity.WeatherEntity;

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


	private final WeatherCommentaryGenerator commentaryGenerator;

	public WeatherMessageResult getWeatherMessage(RegionEntity region) {
		try {
			if (region == null)
				return WeatherMessageResult.defaultMessage();

			WeatherResult weather = getOrFetchWeather(region);

			if (weather.temperature() == null || weather.rainyProb() == null) {
				log.warn("날씨 데이터 불완전 (regionId: {})", region.getRegionId());
				return WeatherMessageResult.defaultMessage();
			}

			WeatherMessage message = commentaryGenerator.generate(weather.temperature(), weather.temperature());

			return new WeatherMessageResult(message.mainMessage(), message.subMessage());

		} catch (Exception e) {
			log.error("날씨 메시지 생성 실패: {}", e.getMessage());
			return WeatherMessageResult.defaultMessage();
		}
	}
}
