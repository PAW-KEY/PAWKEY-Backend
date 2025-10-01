package org.sopt.pawkey.backendapi.global.auth.application.verifier;

import java.net.http.HttpHeaders;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KakaoVerifierService {

	private final RestTemplate restTemplate = new RestTemplate();

	@Value("${social.kakao.userme-uri}")
	private String kakaoUserMeUri;

	public Map<String, String> verifyKakaoToken(String accessToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(accessToken);
		HttpEntity<Void> entity = new HttpEntity<>(headers);

		ResponseEntity<Map> response =
			restTemplate.exchange(kakaoUserMeUri, HttpMethod.GET, entity, Map.class);

		Map<String, Object> body = response.getBody();
		if (body == null) throw new RuntimeException("카카오 응답이 비어 있음");

		Map<String, Object> account = (Map<String, Object>) body.get("kakao_account");
		String email = (String) account.get("email");
		Map<String, Object> profile = (Map<String, Object>) account.get("profile");

		Map<String, String> userInfo = new HashMap<>();
		userInfo.put("platformUserId", String.valueOf(body.get("id")));
		userInfo.put("primaryEmail", email);
		userInfo.put("nickname", (String) profile.get("nickname"));
		userInfo.put("platform", "KAKAO");

		return userInfo;
	}
}