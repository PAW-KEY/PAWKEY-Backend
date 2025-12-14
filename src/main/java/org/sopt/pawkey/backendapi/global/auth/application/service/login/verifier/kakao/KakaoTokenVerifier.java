package org.sopt.pawkey.backendapi.global.auth.application.service.login.verifier.kakao;

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
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoTokenVerifier {

	private final RestTemplate restTemplate;

	@Value("${social.kakao.userme-uri}")
	private String kakaoUserMeUri;

	/**
	 * 카카오 AccessToken을 검증한 후, 사용자 정보를 조회하는 과정
	 */

	public Map<String, String> verifyKakaoToken(String accessToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(accessToken);
		HttpEntity<Void> entity = new HttpEntity<>(headers);

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
		} catch (Exception e) {
			throw new AuthBusinessException(AuthErrorCode.SOCIAL_LOGIN_FAIL);
		}
	}

}