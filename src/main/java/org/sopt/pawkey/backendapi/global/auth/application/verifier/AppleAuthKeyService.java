package org.sopt.pawkey.backendapi.global.auth.application.verifier;


/*
 Apple Developer Key(.p8)를 사용하여 JWT 형태의 Client Secret을 동적으로 생성
 Apple 로그인 요청 시 이 Client Secret을 사용해 Apple 서버와 통신
 */

import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.sopt.pawkey.backendapi.global.auth.exception.AuthBusinessException;
import org.sopt.pawkey.backendapi.global.auth.exception.AuthErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppleAuthKeyService {

	private final RestTemplate restTemplate;
	private final ObjectMapper objectMapper;

	@Value("${social.apple.team-id}")
	private String teamId;

	@Value("${social.apple.key-id}")
	private String keyId;

	@Value("${social.apple.client-id}")
	private String clientId;

	@Value("${social.apple.private-key}")
	private String privateKeyString; // .p8 파일 내용

	@Value("${social.apple.secret-exp-minutes}")
	private long secretExpMinutes;

	@Value("${social.apple.jwks-uri}")
	private String jwksUri; // Apple 공개 키를 조회할 엔드포인트

	private PrivateKey cachedPrivateKey;
	private final Map<String, PublicKey> cachedPublicKeys = new ConcurrentHashMap<>();

	private PrivateKey getPrivateKey() { //Client Secret 서명을 위해 개인 키를 파싱하고 캐싱
		if (cachedPrivateKey != null) { //이미 캐시 변수에 정보 저장되어 있다면
			return cachedPrivateKey;
		}

		try {
			String cleanedKey = privateKeyString
				.replace("-----BEGIN PRIVATE KEY-----", "") // -----BEGIN PRIVATE KEY----- 제거
				.replace("-----END PRIVATE KEY-----", "") // -----END PRIVATE KEY----- 제거
				.replaceAll("\\s", ""); //공백 제거

			// Base64 디코딩
			byte[] keyBytes = Base64.getDecoder().decode(cleanedKey);

			PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(
				keyBytes); //복호화된 keyBytes가 PKCS#8 국제 표준 규격을 따르는 개인 키 데이터임을 명시
			KeyFactory kf = KeyFactory.getInstance("EC"); //EC알고리즘을 사용해 키 생성하는 키 팩토리 객체

			cachedPrivateKey = kf.generatePrivate(spec); //PKCS#8 규격에 따라 키 생성
			return cachedPrivateKey;

		} catch (Exception e) {
			log.error("❌ Apple Private Key 파싱 중 오류 발생: {}", e.getMessage());
			throw new AuthBusinessException(AuthErrorCode.SOCIAL_LOGIN_FAIL);
		}
	}

	public String createClientSecret() { // 서버의 개인 키로 서명된 JWT를 생성
		Date now = new Date();
		Date expiration = Date.from(Instant.now().plusSeconds(secretExpMinutes * 60));

		Map<String, Object> headerParams = new HashMap<>();
		headerParams.put("kid", keyId);

		String clientSecret = Jwts.builder()
			.setHeaderParams(headerParams)
			.setIssuer(teamId)
			.setIssuedAt(now)
			.setExpiration(expiration)
			.setAudience("https://appleid.apple.com")
			.setSubject(clientId)
			.signWith(getPrivateKey(), SignatureAlgorithm.ES256)
			.compact();

		log.debug("Apple JWT Client Secret 생성 완료. 만료: {}", expiration);
		return clientSecret;
	}

	//Apple 공개 키 획득
	public PublicKey getPublicKey(String kid) {
		// 이미 키를 가지고 있으면 Apple 서버에 재요청하지 않고 캐시된 키를 반환
		if (cachedPublicKeys.containsKey(kid)) {
			log.debug("Apple Public Key (kid: {}) 캐시에서 로드됨.", kid);
			return cachedPublicKeys.get(kid);
		}

		//캐시에 공개키 없다면
		log.info("Apple JWKS URI ({})에서 공개 키 조회 시작.", jwksUri);
		String jsonKeys;
		try {
			jsonKeys = restTemplate.getForObject(jwksUri, String.class); //JWKS URI에서 공개 키 JSON 목록 가져옴
			if (jsonKeys == null || jsonKeys.isEmpty()) {
				log.error("❌ Apple JWKS 응답이 비어있습니다.");
				throw new AuthBusinessException(AuthErrorCode.SOCIAL_LOGIN_FAIL);
			}
		} catch (Exception e) {
			log.error("❌ Apple JWKS URI에서 공개 키를 가져오는 중 오류 발생: {}", e.getMessage());
			throw new AuthBusinessException(AuthErrorCode.SOCIAL_LOGIN_FAIL);
		}

		//JWKS JSON(공개키 JSON목록) 파싱 & 키 추출
		try {
			Map<String, Object> keyset = objectMapper.readValue(jsonKeys, new com.fasterxml.jackson.core.type.TypeReference<Map<String, Object>>() {});
			List<Map<String, String>> keys = (List<Map<String, String>>) keyset.get("keys");
			Map<String, String> requiredKey = keys.stream()
				.filter(key -> kid.equals(key.get("kid")))
				.findFirst()
				.orElseThrow(() -> {
					log.warn("⚠️ 요청 kid({})에 일치하는 Apple 공개 키가 JWKS에 없습니다. 캐시 갱신 필요.", kid);
					return new AuthBusinessException(AuthErrorCode.TOKEN_SIGNATURE_INVALID);
				});


			//찾은 키 정보 (n, e)를 사용하여 PublicKey 객체 생성 (RSA 알고리즘 사용)
			String n = requiredKey.get("n"); // modulus
			String e = requiredKey.get("e"); // exponent

			byte[] decodedN = Base64.getUrlDecoder().decode(n);
			byte[] decodedE = Base64.getUrlDecoder().decode(e);

			BigInteger modulus = new BigInteger(1, decodedN);
			BigInteger exponent = new BigInteger(1, decodedE);

			RSAPublicKeySpec keySpec = new RSAPublicKeySpec(modulus, exponent);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");

			PublicKey publicKey = keyFactory.generatePublic(keySpec);


			cachedPublicKeys.put(kid, publicKey);
			log.info("✅ Apple Public Key (kid: {}) 조회 및 캐싱 완료.", kid);
			return publicKey;
		} catch (IOException e) {
			log.error("❌ Apple JWKS JSON 파싱 오류: {}", e.getMessage());
			throw new AuthBusinessException(AuthErrorCode.SOCIAL_LOGIN_FAIL);

		} catch (Exception e) {
			log.error("❌ Apple 공개 키 생성 오류: {}", e.getMessage());
			throw new AuthBusinessException(AuthErrorCode.SOCIAL_LOGIN_FAIL);

		}
	}

	}


