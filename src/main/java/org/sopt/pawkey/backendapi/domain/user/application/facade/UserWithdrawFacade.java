package org.sopt.pawkey.backendapi.domain.user.application.facade;

import org.sopt.pawkey.backendapi.domain.auth.application.service.token.TokenService;
import org.sopt.pawkey.backendapi.domain.auth.application.service.withdraw.SocialWithdrawServiceFactory;
import org.sopt.pawkey.backendapi.domain.auth.domain.Provider;
import org.sopt.pawkey.backendapi.domain.user.application.service.SocialAccountService;
import org.sopt.pawkey.backendapi.domain.user.application.service.UserDeletionService;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserWithdrawFacade {

	//사용자 관련 데이터 삭제
	private final SocialWithdrawServiceFactory withdrawServiceFactory;
	private final SocialAccountService socialAccountService;
	private final TokenService tokenService;
	private final UserDeletionService userDeletionService;

	public void withdraw(Long userId, Provider provider) {

		//revoke 토큰 조회
		String revokeToken = socialAccountService.getRevokeToken(userId, provider);

		tokenService.revokeAllSessions(userId); // 세션 먼저 제거
		userDeletionService.deleteUser(userId); // DB 삭제

		//외부 소셜 연동 해제(실패해도 탈퇴 성공 유지)
		try {
			withdrawServiceFactory.get(provider).withdraw(revokeToken);
		} catch (Exception e) {
			log.warn("소셜 연동 해제 실패 => 내부 탈퇴 진행", e);
		}

	}


}
