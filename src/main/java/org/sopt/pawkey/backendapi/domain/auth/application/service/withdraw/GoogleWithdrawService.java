package org.sopt.pawkey.backendapi.domain.auth.application.service.withdraw;

import org.sopt.pawkey.backendapi.domain.auth.exception.AuthBusinessException;
import org.sopt.pawkey.backendapi.domain.auth.exception.AuthErrorCode;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;
import lombok.RequiredArgsConstructor;

@Service("GOOGLE")
@RequiredArgsConstructor
public class GoogleWithdrawService implements SocialWithdrawService{

	private final RestTemplate restTemplate;

	@Value("${social.google.revoke-uri:https://oauth2.googleapis.com/revoke}")
	private String revokeUri;

	@Override
	public void withdraw(String accessToken) { //토큰 무효화 엔드포인트
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		MultiValueMap<String,String> body = new LinkedMultiValueMap<>();
		body.add("token",accessToken);

		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
		try{
			restTemplate.postForEntity(revokeUri,request, Void.class);
		}
		catch(Exception e){
			throw new AuthBusinessException(AuthErrorCode.SOCIAL_WITHDRAW_FAIL);
		}



	}
}
