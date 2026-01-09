package org.sopt.pawkey.backendapi.domain.weather.application.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

import org.sopt.pawkey.backendapi.domain.region.infra.persistence.entity.RegionEntity;
import org.sopt.pawkey.backendapi.domain.weather.api.dto.WeatherMessageResponse;
import org.sopt.pawkey.backendapi.domain.weather.application.dto.request.WeatherCache;
import org.sopt.pawkey.backendapi.domain.weather.domain.model.WeatherMessage;
import org.sopt.pawkey.backendapi.domain.weather.domain.repository.WeatherRepository;
import org.sopt.pawkey.backendapi.domain.weather.domain.service.WeatherCommentaryGenerator;
import org.sopt.pawkey.backendapi.domain.weather.exception.WeatherBusinessException;
import org.sopt.pawkey.backendapi.domain.weather.exception.WeatherErrorCode;
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

	public WeatherEntity getOrFetchWeather(RegionEntity region) {
		String cacheKey = "weather:" + region.getRegionId();
		WeatherCache cachedData = null;

		try {
			Object cached = redisTemplate.opsForValue().get(cacheKey);
			if (cached instanceof WeatherCache) {
				cachedData = (WeatherCache)cached;
			}
		} catch (Exception e) {
			log.warn(">>>> [Redis Cache Error] 캐시 역직렬화 실패 (regionId: {}): {}", region.getRegionId(), e.getMessage());
			redisTemplate.delete(cacheKey);
		}

		if (cachedData != null) {
			log.info(">>>> [Redis Cache Hit] regionId: {}", region.getRegionId());
			return cachedData.toEntity(region.getRegionId());
		}

		log.info(">>>> [Redis Cache Miss] DB 조회를 시작합니다. regionId: {}", region.getRegionId());
		WeatherEntity weather = findOrCreateWeather(region.getRegionId());

		boolean isIncomplete = (weather.getTemperature() == null || weather.getRainyMm() == null
			|| weather.getRainyProb() == null);

		if (weather.isStale() || isIncomplete) {
			weather = fetchAndUpdateWeather(weather, region);
		}

		long ttl = (weather.getTemperature() == null) ? 60 : getSecondsToNextHour();
		redisTemplate.opsForValue().set(cacheKey, WeatherCache.from(weather), ttl, TimeUnit.SECONDS);

		return weather;
	}

	@Transactional
	public WeatherEntity findOrCreateWeather(Long regionId) {
		return weatherRepository.findByRegionId(regionId)
			.orElseGet(() -> weatherRepository.save(WeatherEntity.builder().regionId(regionId).build()));
	}

	private long getSecondsToNextHour() {
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime nextHour = now.plusHours(1).truncatedTo(ChronoUnit.HOURS);
		return Duration.between(now, nextHour).getSeconds();
	}

	public WeatherEntity fetchAndUpdateWeather(WeatherEntity weather, RegionEntity region) {
		try {
			WeatherResponse response = weatherClient.getCurrentWeather(
				region.getLatitude(), region.getLongitude(), apiKey, "metric"
			);

			if (response == null || response.getConvertedTemp() == null)
				return weather;

			return updateWeatherDetails(weather.getRegionId(), response);

		} catch (Exception e) {
			log.warn("외부 날씨 API 호출 실패. regionId: {}, error: {}", region.getRegionId(), e.getMessage());
			return weather;
		}
	}

	@Transactional
	public WeatherEntity updateWeatherDetails(Long regionId, WeatherResponse response) {
		return weatherRepository.findByRegionId(regionId).map(weather -> {
			weather.updateWeather(
				response.getConvertedTemp(),
				response.getConvertedRain() != null ? response.getConvertedRain() : 0,
				response.getConvertedPop() != null ? response.getConvertedPop() : 0,
				response.getWeatherCode()
			);
			return weather;
		}).orElseThrow(() -> new WeatherBusinessException(WeatherErrorCode.WEATHER_FETCH_FAILED));
	}

	private final WeatherCommentaryGenerator commentaryGenerator;

	public WeatherMessageResponse getWeatherMessage(RegionEntity region) {
		WeatherEntity weather = getOrFetchWeather(region);

		WeatherMessage message = commentaryGenerator.generate(weather.getTemperature(), weather.getRainyProb());

		return WeatherMessageResponse.of(
			message.mainMessage(),
			message.subMessage()
		);
	}
}