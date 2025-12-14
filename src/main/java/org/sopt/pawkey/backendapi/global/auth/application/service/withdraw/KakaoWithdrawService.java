package org.sopt.pawkey.backendapi.global.auth.application.service.withdraw;

import org.springframework.stereotype.Service;

@Service
public class KakaoWithdrawService implements SocialWithdrawService{
	@Override
	public void withdraw(String accessToken) { // POST https://kapi.kakao.com/v1/user/unlink

	}
}
