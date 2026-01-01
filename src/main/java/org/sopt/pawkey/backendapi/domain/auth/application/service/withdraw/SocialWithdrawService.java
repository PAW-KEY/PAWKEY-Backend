package org.sopt.pawkey.backendapi.domain.auth.application.service.withdraw;

public interface SocialWithdrawService {
	void withdraw(String providerToken); //providerToken : 카카오,구글(access token), 애플(refresh token)
}

