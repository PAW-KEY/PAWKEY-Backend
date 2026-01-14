package org.sopt.pawkey.backendapi.domain.weather.infra.cache;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

import java.util.concurrent.TimeUnit;
import java.time.*;

import org.sopt.pawkey.backendapi.domain.weather.application.dto.result.WeatherResult;

@Component
@RequiredArgsConstructor
public class WeatherCacheManager {
	private final RedisTemplate<String, Object> redisTemplate;

	private static final String CACHE_PREFIX = "weather:region:";

	public WeatherResult get(Long regionId) {
		String cacheKey = CACHE_PREFIX + regionId;
		try {
			Object cached = redisTemplate.opsForValue().get(cacheKey);
			return (cached instanceof WeatherResult) ? (WeatherResult)cached : null;
		} catch (Exception e) {
			redisTemplate.delete(cacheKey);
			return null;
		}
	}

	public void save(Long regionId, WeatherResult result, boolean isIncomplete) {
		String cacheKey = CACHE_PREFIX + regionId;
		long ttl = isIncomplete ? 60 : getSecondsToNextHour();
		redisTemplate.opsForValue().set(cacheKey, result, ttl, TimeUnit.SECONDS);
	}

	private long getSecondsToNextHour() {
		LocalDateTime now = LocalDateTime.now();
		return Duration.between(now, now.plusHours(1).truncatedTo(java.time.temporal.ChronoUnit.HOURS)).getSeconds();
	}
}