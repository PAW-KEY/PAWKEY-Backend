package org.sopt.pawkey.backendapi.domain.routes.walktest;

import java.util.UUID;

import org.sopt.pawkey.backendapi.domain.routes.walktest.dto.WalkPointRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@Component
@RequiredArgsConstructor
public class WalkStreamRedisRepository {
	private final RedisTemplate<String, String> redisTemplate;
	private final ObjectMapper objectMapper;

	public String initSession(Long userId, String deviceInfo) {
		String routeId = UUID.randomUUID().toString();

		String metaKey = "walk:session:" + routeId;
		redisTemplate.opsForHash().put(metaKey, "userId", userId.toString());
		redisTemplate.opsForHash().put(metaKey, "device", deviceInfo);
		redisTemplate.opsForHash().put(metaKey, "status", "ACTIVE");

		return routeId;
	}

	public boolean isDuplicated(String routeId, long ts) {
		String key = "walk:" + routeId + ":ts:" + ts;
		Boolean exists = redisTemplate.hasKey(key);
		if (Boolean.TRUE.equals(exists)) return true;

		redisTemplate.opsForValue().set(key, "1");
		return false;
	}

	@SneakyThrows
	public void saveLatestPoint(String routeId, WalkPointRequest req) {
		String key = "walk:" + routeId + ":latest";
		redisTemplate.opsForValue().set(key, objectMapper.writeValueAsString(req));
	}

	public void markSessionEnded(String routeId) {
		redisTemplate.opsForHash().put("walk:session:" + routeId, "status", "END");
	}
}
