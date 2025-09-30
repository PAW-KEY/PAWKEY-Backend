package org.sopt.pawkey.backendapi.global.auth.application.verifier;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.jwk.source.RemoteJWKSet;
import com.nimbusds.jose.proc.BadJOSEException;
import com.nimbusds.jose.proc.JWSKeySelector;
import com.nimbusds.jose.proc.JWSVerificationKeySelector;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.proc.BadJWTException;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.nimbusds.jwt.proc.DefaultJWTClaimsVerifier;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sopt.pawkey.backendapi.global.auth.exception.AuthBusinessException;
import org.sopt.pawkey.backendapi.global.auth.exception.AuthErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Service
public class GoogleVerifierService {

	private static final String GOOGLE_JWKS_URL = "https://www.googleapis.com/oauth2/v3/certs";

	@Value("${social.google.client-id}")
	private String googleClientId;

	private JWKSource<SecurityContext> jwkSource;
	private ConfigurableJWTProcessor<SecurityContext> jwtProcessor;

	/**
	 * 스프링이 @Value 주입 끝낸 후 실행되는 초기화 메서드
	 */
	@PostConstruct
	public void init() {
		try {
			// 1. JWK Source 설정
			this.jwkSource = new RemoteJWKSet<>(new URL(GOOGLE_JWKS_URL));

			// 2. JWT Processor 생성
			this.jwtProcessor = new DefaultJWTProcessor<>();

			// 3. RS256 서명 검증 키 선택자 등록
			JWSKeySelector<SecurityContext> keySelector =
				new JWSVerificationKeySelector<>(JWSAlgorithm.RS256, jwkSource);
			this.jwtProcessor.setJWSKeySelector(keySelector);

			// 4. 필수 클레임 + Audience 검증
			this.jwtProcessor.setJWTClaimsSetVerifier(
				new DefaultJWTClaimsVerifier<>(
					new JWTClaimsSet.Builder().audience(googleClientId).build(),
					new HashSet<>(Arrays.asList("sub", "iss", "exp"))
				)
			);

			log.info("✅ GoogleVerifierService 초기화 완료 (clientId={})", googleClientId);

		} catch (MalformedURLException e) {
			log.error("❌ Google JWKS URL이 잘못되었습니다: {}", GOOGLE_JWKS_URL, e);
			throw new RuntimeException("Google Verifier 초기화 실패", e);
		}
	}

	/**
	 * 구글 ID Token 검증
	 */
	public Map<String, String> verifyGoogleToken(String idToken) {
		try {
			SecurityContext context = null;
			JWTClaimsSet claimsSet = jwtProcessor.process(idToken, context);

			String platformUserId = claimsSet.getSubject();
			String primaryEmail = claimsSet.getStringClaim("email");
			Boolean emailVerified = claimsSet.getBooleanClaim("email_verified");

			if (emailVerified == null || !emailVerified) {
				log.error("❌ Google ID Token: 이메일 인증 안됨");
				throw new AuthBusinessException(AuthErrorCode.SOCIAL_LOGIN_FAIL);
			}

			Map<String, String> userInfo = new HashMap<>();
			userInfo.put("platformUserId", platformUserId);
			userInfo.put("primaryEmail", primaryEmail);
			userInfo.put("platform", "GOOGLE");

			log.info("✅ Google ID Token 검증 성공 (sub={}, email={})", platformUserId, primaryEmail);

			return userInfo;

		} catch (ParseException e) {
			log.error("❌ Google ID Token 파싱 오류", e);
			throw new AuthBusinessException(AuthErrorCode.ACCESS_TOKEN_INVALID);
		} catch (BadJOSEException e) {
			log.error("❌ Google ID Token 검증 실패: {}", e.getMessage());
			throw new AuthBusinessException(AuthErrorCode.SOCIAL_LOGIN_FAIL);
		} catch (Exception e) {
			log.error("❌ Google ID Token 검증 중 예외 발생", e);
			throw new AuthBusinessException(AuthErrorCode.SOCIAL_LOGIN_FAIL);
		}
	}
}
