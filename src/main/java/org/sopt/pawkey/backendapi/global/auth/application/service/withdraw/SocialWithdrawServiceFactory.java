package org.sopt.pawkey.backendapi.global.auth.application.service.withdraw;

import java.util.Map;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SocialWithdrawServiceFactory { //Provider 분기처리

	private final Map<String, SocialWithdrawService> services;

	public SocialWithdrawService get(String provider) {
		return services.get(provider);
	}
}
