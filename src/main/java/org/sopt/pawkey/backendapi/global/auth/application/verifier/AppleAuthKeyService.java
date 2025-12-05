package org.sopt.pawkey.backendapi.global.auth.application.verifier;


/*
 Apple Developer Key(.p8)를 사용하여 JWT 형태의 Client Secret을 동적으로 생성
 Apple 로그인 요청 시 이 Client Secret을 사용해 Apple 서버와 통신
 */

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.sopt.pawkey.backendapi.global.auth.exception.AuthBusinessException;
import org.sopt.pawkey.backendapi.global.auth.exception.AuthErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AppleAuthKeyService {


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

	//매 요청마다 복잡한 파싱과정을 반복하지 않도록,최초 파싱 후 캐싱 변수에 저장
	private PrivateKey cachedPrivateKey;

	private PrivateKey getPrivateKey() {
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
			log.error("Apple Private Key 파싱 중 오류 발생: {}", e.getMessage());
			throw new AuthBusinessException(AuthErrorCode.SOCIAL_LOGIN_FAIL);
		}
	}
		/*
		 - Apple Developer Key를 사용해 JWT Client Secret 생성
		 - 이 Secret은 Apple 인증 API 요청 시 'client_secret' 파라미터로 사용됩니다.
		 * JWT 형태의(String) Client Secret 반환
		 */
		public String createClientSecret() {
			Date now = new Date();
			Date expiration = Date.from(Instant.now().plusSeconds(secretExpMinutes*60));

			Map<String,Object> headerParams = new HashMap<>();
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
	}

