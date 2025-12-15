package org.sopt.pawkey.backendapi.domain.auth.application.service.withdraw;

import java.util.Map;

import org.sopt.pawkey.backendapi.domain.auth.domain.Provider;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SocialWithdrawServiceFactory { // Provider 분기 처리

	private final Map<String, SocialWithdrawService> services;

	public SocialWithdrawService get(Provider provider) {
		return services.get(provider.name());
	}
}