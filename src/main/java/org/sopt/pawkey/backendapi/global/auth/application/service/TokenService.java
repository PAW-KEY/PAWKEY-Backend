package org.sopt.pawkey.backendapi.global.auth.application.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

import org.sopt.pawkey.backendapi.global.auth.api.dto.response.TokenResponseDTO;

import org.sopt.pawkey.backendapi.global.auth.exception.AuthBusinessException;
import org.sopt.pawkey.backendapi.global.auth.exception.AuthErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Date;

import javax.crypto.SecretKey;

@Service
@RequiredArgsConstructor
public class TokenService {

	private final StringRedisTemplate redisTemplate;

	@Value("${security.jwt.secret}") private String secret;
	@Value("${security.jwt.issuer}") private String issuer;
	@Value("${security.jwt.access-exp-minutes}") private long accessExpMinutes;
	@Value("${security.jwt.refresh-exp-days}") private long refreshExpDays;

	private SecretKey signingKey;
	private static final String REFRESH_PREFIX = "refresh:";
	private static final String USER_ID_CLAIM = "userId";
	private static final String DEVICE_ID_CLAIM = "deviceId";

	@PostConstruct
	public void init() {
		// 비밀 키를 Base64로 디코딩하여 Key 객체로 변환
		this.signingKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
	}

	// --- 1. JWT 생성 유틸리티 ---

	/**
	 * Access Token 또는 Refresh Token을 생성합니다.
	 */
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

	/**
	 * Refresh Token을 Redis에 저장합니다.
	 */
	private void saveRefreshToken(Long userId, String deviceId, String refreshToken) {
		String key = REFRESH_PREFIX + userId + ":" + deviceId;
		redisTemplate.opsForValue().set(
			key,
			refreshToken,
			Duration.ofDays(refreshExpDays)
		);
	}

	// --- 2. 핵심 API 메서드 ---

	/**
	 * 1. issueTokens: Access Token과 Refresh Token을 발급하고 Refresh Token을 저장합니다.
	 */
	public TokenResponseDTO issueTokens(Long userId, String deviceId) {
		String accessToken = createToken(userId, deviceId, accessExpMinutes);
		// Refresh Token은 만료 기간을 '일' 단위로 사용
		String refreshToken = createToken(userId, deviceId, refreshExpDays * 24 * 60);

		saveRefreshToken(userId, deviceId, refreshToken);

		return new TokenResponseDTO(accessToken, refreshToken);
	}

	/**
	 * 2. rotate: Refresh Token을 검증하고 새로운 Access/Refresh Token 쌍을 발급합니다.
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

		// 2. Redis에 저장된 Refresh Token과 일치하는지 확인 (Token Rotation 검증)
		if (savedToken == null || !savedToken.equals(oldRefreshToken)) {
			// Redis에 없거나 토큰이 일치하지 않으면 유효하지 않은 토큰 (이미 폐기되었거나 탈취 시도)
			throw new AuthBusinessException(AuthErrorCode.REFRESH_TOKEN_INVALID);
		}

		// 3. 토큰 폐기 및 재발급
		redisTemplate.delete(redisKey); // 기존 토큰 삭제 (사용 후 폐기)
		return issueTokens(userId, deviceId); // 새로운 토큰 발급 및 Redis 저장
	}

	/**
	 * 3. revoke: 로그아웃 처리 (Refresh Token 삭제)
	 */
	public void revoke(Long userId, String deviceId) {
		String key = REFRESH_PREFIX + userId + ":" + deviceId;
		redisTemplate.delete(key);
	}

	// --- 4. JWT 파싱 및 검증 ---

	/**
	 * JWT를 파싱하고 유효성을 검증합니다.
	 */
	public Jws<Claims> validateAndParseToken(String token) {
		try {
			// ✅ verifyWith()가 SecretKey 타입을 인식하고 정상 동작
			return Jwts.parser()
				.verifyWith(signingKey) // SecretKey 타입으로 오류 해결
				.build()
				.parseSignedClaims(token);
		} catch (Exception e) {
			throw new AuthBusinessException(AuthErrorCode.REFRESH_TOKEN_INVALID);
		}
	}


	/**
	 * Access Token에서 사용자 ID를 추출합니다.
	 */
	public Long extractUserId(String accessToken) {
		Jws<Claims> claimsJws = validateAndParseToken(accessToken);
		return claimsJws.getBody().get(USER_ID_CLAIM, Long.class);
	}
}