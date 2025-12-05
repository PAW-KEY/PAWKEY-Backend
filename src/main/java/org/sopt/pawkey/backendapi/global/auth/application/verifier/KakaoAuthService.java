package org.sopt.pawkey.backendapi.global.auth.application.verifier;

import java.util.Map;

import org.sopt.pawkey.backendapi.global.auth.exception.AuthBusinessException;
import org.sopt.pawkey.backendapi.global.auth.exception.AuthErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoAuthService { //web 환경에서만 사용됨(/kakao/callback)
	private final RestTemplate restTemplate = new RestTemplate();

	@Value("${social.kakao.token-uri}")
	private String kakaoTokenUri;

	@Value("${social.kakao.client-id}")
	private String clientId;

	// @Value("${social.kakao.client-secret}")
	// private String clientSecret;

	@Value("${social.kakao.redirect-uri}")
	private String redirectUri;

	public String exchangeCodeForAccessToken(String code) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("grant_type", "authorization_code");
		params.add("client_id", clientId);
		//params.add("client_secret", clientSecret);
		params.add("redirect_uri", redirectUri);
		params.add("code", code);

		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

		ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
			kakaoTokenUri,
			HttpMethod.POST,
			request,
			new ParameterizedTypeReference<Map<String, Object>>() {
			}
		);

		Map<String, Object> body = response.getBody();
		if (body == null || !body.containsKey("access_token")) {
			throw new AuthBusinessException(AuthErrorCode.SOCIAL_LOGIN_FAIL);
		}

		String accessToken = (String)body.get("access_token");

		log.warn("[KAKAO RAW ACCESS TOKEN]: {}", accessToken);
		return accessToken;

	}
}
