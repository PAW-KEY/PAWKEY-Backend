package org.sopt.pawkey.backendapi.domain.auth.application.service.token;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Date;
import java.util.Set;

import javax.crypto.SecretKey;

import org.sopt.pawkey.backendapi.domain.auth.api.dto.response.TokenResponseDTO;
import org.sopt.pawkey.backendapi.domain.auth.exception.AuthBusinessException;
import org.sopt.pawkey.backendapi.domain.auth.exception.AuthErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TokenService {

	private final StringRedisTemplate redisTemplate;


	@Value("${security.jwt.secret}")
	private String secret;
	@Value("${security.jwt.issuer}")
	private String issuer;
	@Value("${security.jwt.access-exp-minutes}")
	private long accessExpMinutes;
	@Value("${security.jwt.refresh-exp-days}")
	private long refreshExpDays;

	private SecretKey signingKey;
	private static final String REFRESH_PREFIX = "refresh:";
	private static final String USER_ID_CLAIM = "userId";
	private static final String DEVICE_ID_CLAIM = "deviceId";

	@PostConstruct
	public void init() {
		// HMAC SHA-256 알고리즘에 사용할 SecretKey 초기화
		this.signingKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
	}

	// Access Token 또는 Refresh Token을 생성
	private String createToken(Long userId, String deviceId, long expirationMinutes) {
		Date now = new Date();
		Date expiration = new Date(now.getTime() + expirationMinutes * 60 * 1000);

		return Jwts.builder()
			.setIssuer(issuer)
			.setIssuedAt(now)
			.setExpiration(expiration)
			.claim(USER_ID_CLAIM, userId)
			.claim(DEVICE_ID_CLAIM, deviceId)
			.signWith(signingKey, SignatureAlgorithm.HS256)
			.compact();
	}

	// 발급된 Refresh Token을 Redis에 저장
	private void saveRefreshToken(Long userId, String deviceId, String refreshToken) {
		String key = REFRESH_PREFIX + userId + ":" + deviceId;
		redisTemplate.opsForValue().set(
			key,
			refreshToken,
			Duration.ofDays(refreshExpDays)
		);
	}

	// Access Token과 Refresh Token을 발급하고, Refresh Token을 저장소에 캐시
	public TokenResponseDTO issueTokens(Long userId, String deviceId) {
		String accessToken = createToken(userId, deviceId, accessExpMinutes);
		String refreshToken = createToken(userId, deviceId,
			refreshExpDays * 24 * 60); // Refresh Token은 '일' 단위 만료 기간을 사용

		saveRefreshToken(userId, deviceId, refreshToken);

		return new TokenResponseDTO(accessToken, refreshToken);
	}

	/**
	 * Refresh Token을 검증하고 새로운 Access/Refresh Token 쌍을 발급
	 */
	public TokenResponseDTO rotate(String oldRefreshToken, String deviceId) {
		Jws<Claims> claimsJws = validateAndParseToken(oldRefreshToken);
		Long userId = claimsJws.getBody().get(USER_ID_CLAIM, Long.class);
		String savedDeviceId = claimsJws.getBody().get(DEVICE_ID_CLAIM, String.class);

		// 1. JWT 내부의 Device ID가 요청된 Device ID와 일치하는지 확인
		if (!deviceId.equals(savedDeviceId)) {
			throw new AuthBusinessException(AuthErrorCode.TOKEN_DEVICE_ID_INVALID);
		}

		String redisKey = REFRESH_PREFIX + userId + ":" + deviceId;
		String savedToken = redisTemplate.opsForValue().get(redisKey);

		// 2. Redis에 저장된 Refresh Token과 일치하는지 확인
		if (savedToken == null || !savedToken.equals(oldRefreshToken)) {
			throw new AuthBusinessException(AuthErrorCode.REFRESH_TOKEN_INVALID); // Redis에 없으면 유효하지 않은 토큰으로 간주
		}

		// 3. 토큰 폐기 & 재발급
		redisTemplate.delete(redisKey); // 기존 토큰 삭제 (사용 후 폐기)
		return issueTokens(userId, deviceId); // 새로운 토큰 발급 및 Redis 저장
	}

	/**
	 * 로그아웃 처리: Redis에서 Refresh Token을 삭제
	 */
	public void revokeSession(Long userId, String deviceId) {
		String key = REFRESH_PREFIX + userId + ":" + deviceId;

		Boolean isDeleted = redisTemplate.delete(key);
		if (Boolean.TRUE.equals(isDeleted)) {
			System.out.println("로그아웃 성공: Refresh Token 삭제됨. Key: " + key);
		} else {
			// 토큰이 이미 만료되었거나 존재하지 않아아도 => 로그아웃 자체는 성공.
			System.out.println("로그아웃 처리됨: Refresh Token이 Redis에 존재하지 않음. Key: " + key);
		}
	}

	//탈퇴에서의 revoke
	public void revokeAllSessions(Long userId) {
		String pattern = REFRESH_PREFIX + userId + ":*";
		Set<String> keys = redisTemplate.keys(pattern);
		if (keys != null && !keys.isEmpty()) {
			redisTemplate.delete(keys);
		}
	}


	/**
	 * JWT의 유효성을 검증하고 클레임 정보를 파싱
	 */
	public Jws<Claims> validateAndParseToken(String token) {
		try {
			return Jwts.parser()
				.verifyWith(signingKey)
				.build()
				.parseSignedClaims(token);
		} catch (Exception e) {
			throw new AuthBusinessException(AuthErrorCode.ACCESS_TOKEN_INVALID);
		}
	}

	/**
	 * Access Token에서 사용자 ID를 추출
	 */
	public Long extractUserId(String accessToken) {
		Jws<Claims> claimsJws = validateAndParseToken(accessToken);
		return claimsJws.getBody().get(USER_ID_CLAIM, Long.class);
	}
}