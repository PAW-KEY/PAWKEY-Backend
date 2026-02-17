package org.sopt.pawkey.backendapi.domain.routes.walk.infra.persistence;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.sopt.pawkey.backendapi.domain.routes.walk.domain.model.WalkPoint;
import org.sopt.pawkey.backendapi.domain.routes.walk.domain.model.WalkSession;
import org.sopt.pawkey.backendapi.domain.routes.walk.domain.repository.WalkSessionRepository;
import org.sopt.pawkey.backendapi.domain.routes.walk.exception.WalkBusinessException;
import org.sopt.pawkey.backendapi.domain.routes.walk.exception.WalkErrorCode;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

        redisTemplate.expire(key, Duration.ofHours(5));
        return routeId;
    }

    @Override
    public void bindActiveSession(Long userId, String routeId, long ttlSeconds) {
        String key = "walk:active:" + userId;
        redisTemplate.opsForValue().set(key, routeId, Duration.ofSeconds(ttlSeconds));

    }

    @Override
    public void clearActiveSession(Long userId) {
        redisTemplate.delete("walk:active:" + userId);
    }

    @Override
    public void appendPoint(String routeId, WalkPoint point) {
        String key = "walk:points:" + routeId;

        try{
            String value = objectMapper.writeValueAsString(point);
            redisTemplate.opsForList().rightPush(key,value);

            redisTemplate.expire("walk:session:" + routeId, Duration.ofHours(2));
            redisTemplate.expire(key, Duration.ofHours(2));

        } catch (JsonProcessingException e) {
            throw new WalkBusinessException(WalkErrorCode.WALK_POINT_SERIALIZATION_FAILED);
        }

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
        String metaKey = "walk:session:" +routeId;
        String pointsKey = "walk:points:" + routeId;

        Map<Object,Object> meta = redisTemplate.opsForHash().entries(metaKey); //세션 시작 코드에서, "walk:session:" + routeId;로 넣어둔 해시값들을 담은 Map추출(?)
        if(meta.isEmpty()){
            throw new WalkBusinessException(WalkErrorCode.SESSION_NOT_FOUND);
        }

        Long userId = Long.valueOf((String)meta.get("userId"));
        long startedAt = Long.parseLong((String) meta.get("startedAt"));


        List<String> rawPoints = redisTemplate.opsForList().range(pointsKey,0,-1);
        List<WalkPoint> points = new ArrayList<>();

        if(rawPoints !=null){
            for(String raw: rawPoints){
                try{
                    points.add(objectMapper.readValue(raw,WalkPoint.class)); //JSON String => Java객체(WalkPoint)로 변환(역직렬화)
                } catch (JsonProcessingException e){
                    throw new WalkBusinessException(WalkErrorCode.WALK_POINT_DESERIALIZATION_FAILED);
                }
            }
        }

        WalkSession session = new WalkSession(
                routeId,
                userId,
                LocalDateTime.ofInstant(
                        Instant.ofEpochMilli(startedAt),
                        ZoneId.systemDefault()
                )
        );

        session.getPoints().addAll(points);
        session.end();

        return session;
    }

    @Override
    public void endSession(String routeId) {
        String key = "walk:session:" + routeId;

        Boolean exists = redisTemplate.hasKey(key);
        if (!exists) {
            throw new WalkBusinessException(WalkErrorCode.SESSION_NOT_FOUND);
        }

        redisTemplate.opsForHash().put(key, "status", "ENDED");
        redisTemplate.opsForHash().put(key, "endedAt", String.valueOf(System.currentTimeMillis()));
    }
}
