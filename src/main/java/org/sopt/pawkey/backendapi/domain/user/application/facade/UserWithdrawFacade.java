package org.sopt.pawkey.backendapi.domain.user.application.facade;

import org.sopt.pawkey.backendapi.domain.user.application.service.UserDeletionService;
import org.sopt.pawkey.backendapi.domain.user.application.service.UserService;
import org.sopt.pawkey.backendapi.global.auth.application.service.token.TokenService;
import org.sopt.pawkey.backendapi.global.auth.application.service.withdraw.SocialWithdrawServiceFactory;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserWithdrawFacade {


	//사용자 관련 데이터 삭제
	private final SocialWithdrawServiceFactory withdrawServiceFactory;
	private final TokenService tokenService;
	private final UserDeletionService userDeletionService;

	public void withdraw(Long userId, String provider, String providerToken) {

		// 1. 소셜 연동 해제
		withdrawServiceFactory
			.get(provider)
			.withdraw(providerToken);

		// 2. 모든 세션 무효화
		tokenService.revokeAll(userId);

		// 3. 사용자 데이터 삭제
		userDeletionService.deleteUser(userId);
	}


}
