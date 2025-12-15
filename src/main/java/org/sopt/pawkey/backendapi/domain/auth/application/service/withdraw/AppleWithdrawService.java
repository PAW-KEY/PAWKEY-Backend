package org.sopt.pawkey.backendapi.domain.auth.application.service.withdraw;

import org.sopt.pawkey.backendapi.domain.auth.application.service.login.verifier.apple.AppleAuthKeyService;
import org.sopt.pawkey.backendapi.domain.auth.exception.AuthBusinessException;
import org.sopt.pawkey.backendapi.domain.auth.exception.AuthErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;

@Service("APPLE")
@RequiredArgsConstructor
public class AppleWithdrawService implements SocialWithdrawService{

	private final AppleAuthKeyService appleAuthKeyService;
	private final RestTemplate restTemplate;

	@Value("${social.apple.revoke-uri:https://appleid.apple.com/auth/revoke}")
	private String revokeUri;

	@Override
	public void withdraw(String refreshToken) {
		MultiValueMap<String, String> params = appleAuthKeyService.createRevokeParams(refreshToken);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

		try {
			restTemplate.postForEntity(revokeUri, request, Void.class);
		} catch (Exception e) {
			throw new AuthBusinessException(AuthErrorCode.SOCIAL_WITHDRAW_FAIL);
		}
	}
}
