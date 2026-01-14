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

	public WeatherResult get(Long regionId) {
		try {
			Object cached = redisTemplate.opsForValue().get("weather:" + regionId);
			return (cached instanceof WeatherResult) ? (WeatherResult)cached : null;
		} catch (Exception e) {
			redisTemplate.delete("weather:" + regionId);
			return null;
		}
	}

	public void save(Long regionId, WeatherResult result, boolean isIncomplete) {
		long ttl = isIncomplete ? 60 : getSecondsToNextHour();
		redisTemplate.opsForValue().set("weather:" + regionId, result, ttl, TimeUnit.SECONDS);
	}

	private long getSecondsToNextHour() {
		LocalDateTime now = LocalDateTime.now();
		return Duration.between(now, now.plusHours(1).truncatedTo(java.time.temporal.ChronoUnit.HOURS)).getSeconds();
	}
}