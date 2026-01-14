package org.sopt.pawkey.backendapi.domain.weather.application.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

import org.sopt.pawkey.backendapi.domain.region.infra.persistence.entity.RegionEntity;
import org.sopt.pawkey.backendapi.domain.weather.application.dto.request.WeatherResult;
import org.sopt.pawkey.backendapi.domain.weather.domain.repository.WeatherRepository;
import org.sopt.pawkey.backendapi.domain.weather.infra.persistence.entity.WeatherEntity;
import org.sopt.pawkey.backendapi.global.infra.external.weather.WeatherClient;
import org.sopt.pawkey.backendapi.global.infra.external.weather.dto.OpenWeatherResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class WeatherService {

	private final WeatherRepository weatherRepository;
	private final WeatherClient weatherClient;

	private final RedisTemplate<String, Object> redisTemplate;

	@Value("${weather.api-key}")
	private String apiKey;

	@Transactional
	public WeatherResult getOrFetchWeather(RegionEntity region) {
		String cacheKey = "weather:" + region.getRegionId();

		try {
			Object cached = redisTemplate.opsForValue().get(cacheKey);
			if (cached instanceof WeatherResult cachedData) {
				log.info(">>>> [Redis Cache Hit] regionId: {}", region.getRegionId());
				return cachedData;
			}
		} catch (Exception e) {
			log.warn(">>>> [Redis Cache Error] 역직렬화 실패: {}", e.getMessage());
			redisTemplate.delete(cacheKey);
		}

		WeatherEntity weather = weatherRepository.findByRegionId(region.getRegionId())
			.orElse(null);

		if (weather == null) {
			weather = fetchAndSaveNewWeather(region);
		} else if (weather.isStale() || isIncomplete(weather)) {
			fetchAndUpdateWeather(weather, region);
		}

		WeatherResult result = WeatherResult.from(weather);
		long ttl = isIncomplete(weather) ? 60 : getSecondsToNextHour();
		redisTemplate.opsForValue().set(cacheKey, result, ttl, TimeUnit.SECONDS);

		return result;
	}

	private WeatherEntity fetchAndSaveNewWeather(RegionEntity region) {
		OpenWeatherResponse response = fetchFromApi(region);
		return weatherRepository.save(WeatherEntity.create(
			region.getRegionId(),
			response.getConvertedTemp(),
			response.getConvertedRain() != null ? response.getConvertedRain() : 0,
			response.getConvertedPop() != null ? response.getConvertedPop() : 0,
			response.getWeatherCode()
		));
	}

	private OpenWeatherResponse fetchFromApi(RegionEntity region) {
		return weatherClient.getCurrentWeather(
			region.getLatitude(), region.getLongitude(), apiKey, "metric"
		);
	}

	private boolean isIncomplete(WeatherEntity weather) {
		return weather.getTemperature() == null || weather.getRainyMm() == null || weather.getRainyProb() == null;
	}

	private long getSecondsToNextHour() {
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime nextHour = now.plusHours(1).truncatedTo(ChronoUnit.HOURS);
		return Duration.between(now, nextHour).getSeconds();
	}

	private void fetchAndUpdateWeather(WeatherEntity weather, RegionEntity region) {
		try {
			OpenWeatherResponse response = fetchFromApi(region);
			if (response.getConvertedTemp() != null) {
				weather.updateWeather(
					response.getConvertedTemp(),
					response.getConvertedRain() != null ? response.getConvertedRain() : 0,
					response.getConvertedPop() != null ? response.getConvertedPop() : 0,
					response.getWeatherCode()
				);
			}
		} catch (Exception e) {
			log.warn("날씨 업데이트 실패: {}", e.getMessage());
		}
	}
}