package org.sopt.pawkey.backendapi.global.auth.application.verifier;

import java.util.HashMap;
import java.util.Map;

import org.sopt.pawkey.backendapi.global.auth.exception.AuthBusinessException;
import org.sopt.pawkey.backendapi.global.auth.exception.AuthErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoVerifierService {

	private final RestTemplate restTemplate = new RestTemplate();

	@Value("${social.kakao.userme-uri}")
	private String kakaoUserMeUri;

	/**
	 * 카카오 AccessToken을 검증한 후, 사용자 정보를 조회하는 과정
	 */

	public Map<String, String> verifyKakaoToken(String rawAccessToken) {
		String accessToken = rawAccessToken.trim();
		log.info("[Kakao Verifier] 요청 토큰 길이: {}", accessToken.length());

		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(accessToken);
		HttpEntity<Void> entity = new HttpEntity<>(headers);

		log.info("[Kakao Verifier] Authorization 헤더: {}", headers.getFirst(HttpHeaders.AUTHORIZATION));

		try {
			ResponseEntity<Map> response =
				restTemplate.exchange(kakaoUserMeUri, HttpMethod.GET, entity, Map.class);

			Map<String, Object> body = response.getBody();
			if (body == null) {
				throw new AuthBusinessException(AuthErrorCode.SOCIAL_LOGIN_FAIL);
			}

			String platformUserId = String.valueOf(body.get("id"));

			//계정 정보
			Map<String, Object> account = (Map<String, Object>)body.get("kakao_account");
			String email = account != null ? (String)account.get("email") : null;

			// 프로필 (응답구조상, JSON)
			Map<String, Object> profile = account != null ? (Map<String, Object>)account.get("profile") : null;

			//닉네임
			String nickname = profile != null ? (String)profile.get("nickname") : "KakaoUser";

			// 이메일
			if (email == null) {
				email = "kakao_" + platformUserId + "@temp.com";
				log.warn("⚠️ Kakao 계정에 이메일이 없어 임시 이메일을 생성합니다. id={}, fallback={}", platformUserId, email);
			}

			Map<String, String> userInfo = new HashMap<>();
			userInfo.put("platformUserId", platformUserId);
			userInfo.put("primaryEmail", email);
			userInfo.put("nickname", nickname);
			userInfo.put("platform", "KAKAO");

			return userInfo;
		}catch (HttpClientErrorException e) { // 🚨 HTTP 응답 코드를 잡기 위한 예외 처리 수정
			log.error("카카오 API 통신 실패 (HTTP 상태 코드: {}): {}", e.getStatusCode(), e.getResponseBodyAsString());
			throw new AuthBusinessException(AuthErrorCode.SOCIAL_LOGIN_FAIL);
		} catch (Exception e) {
			log.error("카카오 API 통신 중 예기치 않은 오류 발생: {}", e.getMessage(), e);
			throw new AuthBusinessException(AuthErrorCode.SOCIAL_LOGIN_FAIL);
		}
	}
}