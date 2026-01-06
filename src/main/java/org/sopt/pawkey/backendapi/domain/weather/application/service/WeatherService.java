package org.sopt.pawkey.backendapi.domain.weather.application.service;

import java.util.concurrent.TimeUnit;

import org.sopt.pawkey.backendapi.domain.region.infra.persistence.entity.RegionEntity;
import org.sopt.pawkey.backendapi.domain.weather.application.dto.request.WeatherCache;
import org.sopt.pawkey.backendapi.domain.weather.domain.repository.WeatherRepository;
import org.sopt.pawkey.backendapi.domain.weather.infra.persistence.entity.WeatherEntity;
import org.sopt.pawkey.backendapi.global.infra.external.weather.WeatherClient;
import org.sopt.pawkey.backendapi.global.infra.external.weather.dto.WeatherResponse;
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
	public WeatherEntity getOrFetchWeather(RegionEntity region) {
		String cacheKey = "weather:" + region.getRegionId();

		WeatherCache cachedData = (WeatherCache)redisTemplate.opsForValue().get(cacheKey);

		if (cachedData != null) {
			log.info(">>>> [Redis Cache Hit] regionId: {}", region.getRegionId());
			return cachedData.toEntity(region.getRegionId());
		}

		log.info(">>>> [Redis Cache Miss] DB 조회를 시작합니다. regionId: {}", region.getRegionId());
		WeatherEntity weather = weatherRepository.findByRegionId(region.getRegionId())
			.orElseGet(() -> createNewWeather(region.getRegionId()));

		if (weather.isStale() || weather.getTemperature() == null) {
			fetchAndUpdateWeather(weather, region);
		}

		redisTemplate.opsForValue().set(cacheKey, WeatherCache.from(weather), 1, TimeUnit.HOURS);

		return weather;
	}

	private void fetchAndUpdateWeather(WeatherEntity weather, RegionEntity region) {
		WeatherResponse response = weatherClient.getCurrentWeather(
			region.getLatitude(), region.getLongitude(), apiKey, "metric"
		);

		weather.updateWeather(
			response.getConvertedTemp(),
			response.getConvertedRain(),
			response.getConvertedPop(),
			response.getWeatherCode()
		);
	}

	private WeatherEntity createNewWeather(Long regionId) {
		return weatherRepository.save(WeatherEntity.builder().regionId(regionId).build());
	}
}