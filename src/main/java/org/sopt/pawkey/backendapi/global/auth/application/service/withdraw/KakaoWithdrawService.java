package org.sopt.pawkey.backendapi.global.auth.application.service.withdraw;

import org.sopt.pawkey.backendapi.global.auth.exception.AuthBusinessException;
import org.sopt.pawkey.backendapi.global.auth.exception.AuthErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;

@Service("KAKAO")
@RequiredArgsConstructor
public class KakaoWithdrawService implements SocialWithdrawService{

	private final RestTemplate restTemplate;

	@Value("${social.kakao.unlink-uri:https://kapi.kakao.com/v1/user/unlink}")
	private String unlinkUri;

	@Override
	public void withdraw(String accessToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(accessToken);

		HttpEntity<Void> request = new HttpEntity<>(headers);

		try {
			restTemplate.exchange(unlinkUri, HttpMethod.POST, request, Value.class);
		} catch (Exception e) {
			throw new AuthBusinessException(AuthErrorCode.SOCIAL_WITHDRAW_FAIL);
		}
	}
}
