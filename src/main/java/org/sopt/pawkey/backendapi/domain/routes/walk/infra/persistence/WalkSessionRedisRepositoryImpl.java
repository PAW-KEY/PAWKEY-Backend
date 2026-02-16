package org.sopt.pawkey.backendapi.domain.routes.walk.infra.persistence;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.sopt.pawkey.backendapi.domain.routes.walk.domain.model.WalkPoint;
import org.sopt.pawkey.backendapi.domain.routes.walk.domain.model.WalkSession;
import org.sopt.pawkey.backendapi.domain.routes.walk.domain.repository.WalkSessionRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.UUID;


@Repository
@RequiredArgsConstructor
public class WalkSessionRedisRepositoryImpl implements WalkSessionRepository {
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;


    @Override
    public boolean existsActiveSession(Long userId) {
        return redisTemplate.hasKey("walk:active:" + userId);
    }

    @Override
    public String createSession(Long userId, String deviceInfo, long startedAt) {
        String routeId = UUID.randomUUID().toString();
        String key = "walk:session:" + routeId;

        //세션 메타정보 -> Hash자료구조 (속성 여러 개)
        redisTemplate.opsForHash().put(key, "userId", userId.toString());
        redisTemplate.opsForHash().put(key, "device", deviceInfo);
        redisTemplate.opsForHash().put(key, "status", "ACTIVE");
        redisTemplate.opsForHash().put(key, "startedAt", String.valueOf(startedAt));

        return routeId;
    }

    @Override
    public void bindActiveSession(Long userId, String routeId, long ttlSeconds) {
        String key = "walk:active:" + userId;
        redisTemplate.opsForValue().set(key, routeId, Duration.ofSeconds(ttlSeconds));

    }

    @Override
    public void appendPoint(String routeId, WalkPoint point) {

    }

    @Override
    public boolean isDuplicated(String routeId, long timestamp) {
        String key = "walk:dedup:" + routeId + ":" + timestamp;
        Boolean exists = redisTemplate.hasKey(key);
        if(exists) return true;

        redisTemplate.opsForValue().set(key, "1", Duration.ofSeconds(60));
        return false;
    }

    @Override
    public WalkSession loadSession(String routeId) {
        return null;
    }

    @Override
    public void endSession(String routeId) {

    }
}
