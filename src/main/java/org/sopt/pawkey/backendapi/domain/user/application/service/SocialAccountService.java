package org.sopt.pawkey.backendapi.domain.user.application.service;

import org.sopt.pawkey.backendapi.domain.auth.domain.repository.AppleRefreshTokenRepository;
import org.sopt.pawkey.backendapi.domain.user.domain.repository.SocialAccountRepository;
import org.springframework.stereotype.Service;
import org.sopt.pawkey.backendapi.domain.auth.domain.Provider;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SocialAccountService {
	private final SocialAccountRepository socialAccountRepository;
	private final AppleRefreshTokenRepository appleRefreshTokenRepository;

	//탈퇴 시 외부 revoke에 필요한 토큰 조회(애플 -> 서버 처리 / 카카오,구글 -> 클라 sdk책임)
	public String getRevokeToken(Long userId, Provider provider) {
		if (provider == Provider.APPLE) {
			return appleRefreshTokenRepository.findById(userId)
				.orElseThrow(() ->
					new IllegalStateException("Apple refresh token 없음")
				)
				.getRefreshToken();
		}

		// KAKAO / GOOGLE
		socialAccountRepository.findByUser_UserIdAndPlatform(
			userId, provider.name()
		).orElseThrow(() ->
			new IllegalStateException("소셜 계정 정보 없음")
		);

		return null;
	}

}
