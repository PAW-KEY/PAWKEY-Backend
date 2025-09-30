package org.sopt.pawkey.backendapi.domain.user.application.facade;

import java.util.Map;

import org.sopt.pawkey.backendapi.domain.user.application.service.UserService;
import org.sopt.pawkey.backendapi.global.auth.api.dto.response.TokenResponseDTO;
import org.sopt.pawkey.backendapi.global.auth.application.service.TokenService;
import org.sopt.pawkey.backendapi.global.auth.application.verifier.GoogleVerifierService;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserLoginFacade {

	private final UserService userService;
	private final TokenService tokenService;
	private final GoogleVerifierService googleVerifierService;
	public TokenResponseDTO googleLogin(String idToken, String deviceId) {
		// 1. 외부 시스템 연동 (Verifier는 4단계에서 구현 예정)
		Map<String, String> socialUserInfo = googleVerifierService.verifyGoogleToken(idToken);


		// 임시 더미 데이터 (Verifier 구현 전까지 사용)
		String platformUserId = "dummyGoogleId12345";
		String primaryEmail = "dummy@google.com";

		// 2. 비즈니스 처리 (User 생성/조회)
		// UserService는 User와 SocialAccount의 DB 트랜잭션만 책임집니다.
		Long userId = userService.findOrCreateUserBySocialId("GOOGLE", platformUserId, primaryEmail);

		// 3. 인프라 처리 (토큰 발급 및 Redis 저장)
		// TokenService는 AccessToken과 RefreshToken이 담긴 DTO를 반환합니다.
		return tokenService.issueTokens(userId, deviceId);
	}

}
