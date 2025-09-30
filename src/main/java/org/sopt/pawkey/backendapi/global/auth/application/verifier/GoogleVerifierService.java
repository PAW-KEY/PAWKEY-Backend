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


// @Slf4j
// @Service
// @RequiredArgsConstructor
// public class GoogleVerifierService {
// 	// Google JWKS 엔드포인트 URL
// 	private static final String GOOGLE_JWKS_URL = "https://www.googleapis.com/oauth2/v3/certs";
//
// 	// 서명 알고리즘: Google은 RS256을 사용합니다.
// 	private static final Set<JWSAlgorithm> JWS_ALGORITHMS = Collections.singleton(JWSAlgorithm.RS256);
//
// 	@Value("${social.google.client-id}")
// 	private String googleClientId;
//
// 	// --- JWKSource 및 JWTProcessor 초기화 ---
//
// 	// JWKS 세트 (Google의 공개 키 목록)
// 	private JWKSource<SecurityContext> jwkSource;
//
// 	// JWT 프로세서: 토큰 검증 로직을 담당
// 	private ConfigurableJWTProcessor<SecurityContext> jwtProcessor;
//
// 	// @PostConstruct 대신 생성자에서 초기화 (MalformedURLException 처리)
// 	public GoogleVerifierService(
// 		@Value("${social.google.client-id}") String googleClientId) {
// 		this.googleClientId = googleClientId;
// 		try {
// 			// 1. JWK Source 설정: Google의 JWKS URL에서 공개 키 세트를 가져옵니다.
// 			this.jwkSource = new RemoteJWKSet<>(new URL(GOOGLE_JWKS_URL));
//
// 			// 2. JWT Processor 설정
// 			this.jwtProcessor = new DefaultJWTProcessor<>();
//
// 			// 3. JWS 키 선택자 설정: 토큰의 서명 알고리즘(RS256)을 사용하여 키를 선택합니다.
// 			JWSKeySelector<SecurityContext> keySelector =
// 				new JWSVerificationKeySelector<>(JWS_ALGORITHMS, jwkSource);
// 			this.jwtProcessor.setJWSKeySelector(keySelector);
//
// 			// 4. 클레임 검증: 유효성(만료 시간, 발급자, 수신자)을 검증하는 리스너 설정
// 			// 발급자(Issuer)는 accounts.google.com 또는 https://accounts.google.com
// 			// 수신자(Audience)는 우리 서비스의 Google Client ID
// 			this.jwtProcessor.setJWTClaimsSetVerifier(
// 				new DefaultJWTClaimsVerifier<>(
// 					new JWTClaimsSet.Builder().audience(googleClientId).build(),
// 					new HashSet<>(Arrays.asList("sub", "iss", "exp")) // 필수 클레임 지정
// 				)
// 			);
//
// 		} catch (MalformedURLException e) {
// 			log.error("Google JWKS URL이 잘못되었습니다: {}", GOOGLE_JWKS_URL, e);
// 			// 서비스 시작 불가 예외 처리 (배포 환경에서 치명적)
// 			throw new RuntimeException("Google Verifier 초기화 실패: URL 오류", e);
// 		}
// 	}
//
// 	// --- ID Token 검증 및 정보 추출 메서드 ---
//
// 	public Map<String, String> verifyGoogleToken(String idToken) {
// 		try {
// 			// 1. 토큰 검증 및 클레임 추출
// 			SecurityContext context = null;
// 			JWTClaimsSet claimsSet = jwtProcessor.process(idToken, context);
//
// 			// 2. 클레임에서 필요한 정보 추출
// 			String platformUserId = claimsSet.getSubject(); // Google의 'sub' 클레임 (고유 ID)
// 			String primaryEmail = claimsSet.getStringClaim("email"); // 이메일
// 			Boolean emailVerified = claimsSet.getBooleanClaim("email_verified");
// 			if (emailVerified == null || !emailVerified) {
// 				throw new AuthBusinessException(AuthErrorCode.SOCIAL_LOGIN_FAIL);
// 			}
//
// 			if (!"true".equals(emailVerified)) {
// 				log.error("Google ID Token: 인증되지 않은 이메일입니다.");
// 				throw new AuthBusinessException(AuthErrorCode.SOCIAL_LOGIN_FAIL);
// 			}
//
// 			// 3. 사용자 정보 맵으로 반환
// 			Map<String, String> userInfo = new HashMap<>();
// 			userInfo.put("platformUserId", platformUserId);
// 			userInfo.put("primaryEmail", primaryEmail);
// 			userInfo.put("platform", "GOOGLE");
// 			return userInfo;
//
// 		} catch (ParseException e) {
// 			log.error("Google ID Token 파싱 오류: 토큰 형식이 잘못되었습니다.", e);
// 			throw new AuthBusinessException(AuthErrorCode.ACCESS_TOKEN_INVALID);
// 		} catch (BadJOSEException e) {
// 			// 서명 오류, 만료, audience 불일치 등 유효성 검증 실패
// 			log.error("Google ID Token 유효성 검증 실패: {}", e.getMessage(), e);
// 			throw new AuthBusinessException(AuthErrorCode.SOCIAL_LOGIN_FAIL);
// 		} catch (Exception e) {
// 			log.error("Google ID Token 검증 중 예상치 못한 오류 발생", e);
// 			throw new AuthBusinessException(AuthErrorCode.SOCIAL_LOGIN_FAIL);
// 		}
// 	}
// }
