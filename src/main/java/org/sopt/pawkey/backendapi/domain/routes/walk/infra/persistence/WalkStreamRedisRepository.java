package org.sopt.pawkey.backendapi.domain.routes.walk.infra.persistence;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

import org.sopt.pawkey.backendapi.domain.routes.walk.api.dto.request.WalkPointRequestDTO;
import org.sopt.pawkey.backendapi.domain.routes.walk.application.dto.command.AppendWalkPointCommand;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
@Component
@RequiredArgsConstructor
public class WalkStreamRedisRepository {

	private final RedisTemplate<String, String> redisTemplate;
	private static final Duration SESSION_TTL = Duration.ofHours(5);
	private final ObjectMapper objectMapper;

	public String initSession(Long userId, String deviceInfo) {
		String routeId = UUID.randomUUID().toString();
		String key = "walk:session:" + routeId;

		redisTemplate.opsForHash().put(key, "userId", userId.toString());
		redisTemplate.opsForHash().put(key, "device", deviceInfo);
		redisTemplate.opsForHash().put(key, "status", "ACTIVE");
		redisTemplate.opsForHash().put(key, "startedAt", String.valueOf(System.currentTimeMillis()));

		redisTemplate.expire(key, SESSION_TTL); //TTL
		return routeId;
	}


	public boolean existsActiveSession(Long userId){
		return redisTemplate.hasKey("walk:active:" + userId);
	}

	public void bindActiveSession(Long userId, String routeId, Duration ttl){
		redisTemplate.opsForValue()
				.set(
						"walk:active:" + userId, //key설정
						routeId, //값 설정
						ttl
				);
	}

//	public boolean isDuplicated(String routeId, long ts) {
//		String key = "walk:dedup:" + routeId + ":" + ts;
//		if (Boolean.TRUE.equals(redisTemplate.hasKey(key))) return true;
//		redisTemplate.opsForValue().set(key, "1", Duration.ofMinutes(10));
//		return false;
//	}
//
//	@SneakyThrows
//	public void appendPoint(AppendWalkPointCommand command) {
//		String key = "walk:points:" + command.routeId();
//		redisTemplate.opsForList().rightPush(key, objectMapper.writeValueAsString(command));
//	}
//
//	public List<WalkPoint> getAllPoints(String routeId) {
//		List<String> raw = redisTemplate.opsForList().range("walk:points:" + routeId, 0, -1);
//		return raw.stream()
//				.map(s -> objectMapper.readValue(s, WalkPoint.class))
//				.toList();
//	}
//
//	public void markSessionEnded(String routeId) {
//		redisTemplate.opsForHash().put("walk:session:" + routeId, "status", "END");
//	}
}
