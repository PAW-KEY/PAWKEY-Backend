package org.sopt.pawkey.backendapi.global.auth.application.verifier;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException; // io.jsonwebtoken.SignatureException을 import합니다.
import io.jsonwebtoken.io.Decoders;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

// 커스텀 예외 및 오류 코드
import org.sopt.pawkey.backendapi.global.auth.exception.AuthBusinessException;
import org.sopt.pawkey.backendapi.global.auth.exception.AuthErrorCode;
// 의존성 서비스 (AppleAuthKeyService)


import java.security.Key;
import java.security.PublicKey; // 명확성을 위해 PublicKey를 명시적으로 import합니다.
import java.util.Map;
import java.util.HashMap;
@Slf4j
@Service
@RequiredArgsConstructor
public class AppleVerifierService {
	private final RestTemplate restTemplate;
	private final ObjectMapper objectMapper;
	private final AppleAuthKeyService appleAuthKeyService;

	@Value("${social.apple.client-id}")
	private String clientId;

	@Value("${social.apple.token-uri}")
	private String appleTokenUri;

	@Value("${social.apple.redirect-uri}")
	private String redirectUri;

	//클라이언트가 로그인 후 서버로 보낸 ID Token 검증
	public Map<String, String> verifyAppleIdToken(String idToken) {
		Claims claims = validateIdTokenSignatureAndGetClaims(idToken);

		String platformUserId = claims.getSubject();
		String email = claims.get("email", String.class);

		if (platformUserId == null || platformUserId.isEmpty()) {
			log.error("❌ Apple ID Token에서 필수 사용자 ID(sub)가 추출되지 않았습니다.");
			throw new AuthBusinessException(AuthErrorCode.SOCIAL_ID_INVALID);
		}

		if (email == null || email.isEmpty()) {
			log.warn("⚠️ Apple 계정에 이메일 정보가 없어 임시 이메일을 생성합니다. ID: {}", platformUserId);
			email = "apple_" + platformUserId + "@temp.com";
		}

		Map<String, String> userInfo = new HashMap<>();
		userInfo.put("platformUserId", platformUserId);
		userInfo.put("primaryEmail", email);
		userInfo.put("platform", "APPLE");

		return userInfo;
	}

	//Apple ID Token (JWT)의 서명을 검증하고 Claims를 반환
	private Claims validateIdTokenSignatureAndGetClaims(String idToken) {
		try {
			//JWT Header 디코딩
			String[] parts = idToken.split("\\.");
			if (parts.length != 3) {
				log.error("❌ ID Token 형식 오류: JWT의 점(.) 분리자가 3개가 아닙니다. parts.length: {}", parts.length);
				throw new AuthBusinessException(AuthErrorCode.ACCESS_TOKEN_INVALID);
			}
			String headerJson = new String(Decoders.BASE64URL.decode(parts[0]));
			Map<String, Object> header = objectMapper.readValue(headerJson, Map.class);
			String kid = (String)header.get("kid");

			if (kid == null) {
				log.error("❌ ID Token Header에 kid(Key ID)가 누락되었습니다.");
				throw new AuthBusinessException(AuthErrorCode.TOKEN_SIGNATURE_INVALID);
			}

			// 키 획득
			PublicKey applePublicKey = appleAuthKeyService.getPublicKey(kid);

			if (applePublicKey == null) {
				log.error("❌ AuthKeyService가 Apple Public Key (kid: {})를 가져오는 데 실패했습니다.", kid);
				throw new AuthBusinessException(AuthErrorCode.TOKEN_SIGNATURE_INVALID);
			}

			//서명 검증 & Claims 파싱
			return Jwts.parser()
				.requireIssuer("https://appleid.apple.com") // iss 검증 (발급자: Apple)
				.requireAudience(clientId) // aud 검증 (수신자: 우리 서비스)
				.verifyWith(applePublicKey) //  PublicKey 타입 객체로 서명 검증.
				.build()
				.parseSignedClaims(idToken)
				.getPayload();

		} catch (ExpiredJwtException e) {
			// 토큰 만료
			log.warn("⚠️ Apple ID Token 만료: {}", e.getMessage());
			throw new AuthBusinessException(AuthErrorCode.TOKEN_SIGNATURE_INVALID);
		} catch (SignatureException e) { // <<-- io.jsonwebtoken.SignatureException 처리
			// 서명이 일치하지 않음
			log.error("❌ Apple ID Token 서명 오류: {}", e.getMessage());
			throw new AuthBusinessException(AuthErrorCode.TOKEN_SIGNATURE_INVALID);
		} catch (MalformedJwtException e) {
			log.error("❌ Apple ID Token 구조 오류: {}", e.getMessage());
			throw new AuthBusinessException(AuthErrorCode.ACCESS_TOKEN_INVALID);
		} catch (AuthBusinessException e) {
			throw e;
		} catch (Exception e) {

			log.error("❌ Apple ID Token 검증 중 예기치 않은 오류 발생: {}", e.getMessage(), e);
			throw new AuthBusinessException(AuthErrorCode.SOCIAL_LOGIN_FAIL);
		}
	}

	public Map<String, Object> exchangeCodeForTokens(String code) {
		String clientSecret = appleAuthKeyService.createClientSecret();

		try {
			MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
			body.add("client_id", clientId);
			body.add("client_secret", clientSecret);
			body.add("code", code);
			body.add("grant_type", "authorization_code");
			body.add("redirect_uri", redirectUri);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

			HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(body, headers);

			ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
				appleTokenUri,
				HttpMethod.POST,
				entity,
				new ParameterizedTypeReference<Map<String, Object>>() {
				}
			);

			log.info("Apple Token 교환 성공");
			return response.getBody();

		} catch (HttpClientErrorException e) {
			log.error("❌ Apple Token 교환 실패 (HTTP 상태 코드: {}): 응답 바디: {}", e.getStatusCode(),
				e.getResponseBodyAsString());
			throw new AuthBusinessException(AuthErrorCode.SOCIAL_LOGIN_FAIL);
		} catch (Exception e) {
			log.error("❌ Exception {}", e.getMessage(), e);
			throw new AuthBusinessException(AuthErrorCode.SOCIAL_LOGIN_FAIL);
		}
	}
}