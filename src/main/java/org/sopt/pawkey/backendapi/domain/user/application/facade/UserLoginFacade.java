package org.sopt.pawkey.backendapi.domain.user.application.facade;

import java.util.Map;

import org.sopt.pawkey.backendapi.domain.user.application.service.UserService;
import org.sopt.pawkey.backendapi.global.auth.api.dto.response.TokenResponseDTO;
import org.sopt.pawkey.backendapi.global.auth.application.service.TokenService;
import org.sopt.pawkey.backendapi.global.auth.application.verifier.AppleVerifierService;
import org.sopt.pawkey.backendapi.global.auth.application.verifier.GoogleVerifierService;
import org.sopt.pawkey.backendapi.global.auth.application.verifier.KakaoVerifierService;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserLoginFacade {

	private final UserService userService;
	private final TokenService tokenService;
	private final GoogleVerifierService googleVerifierService;
	private final KakaoVerifierService kakaoVerifierService;
	private final AppleVerifierService appleVerifierService;
	public TokenResponseDTO googleLogin(String idToken, String deviceId) {
		Map<String, String> socialUserInfo = googleVerifierService.verifyGoogleToken(idToken);

		String platformUserId = socialUserInfo.get("platformUserId");
		String primaryEmail = socialUserInfo.get("primaryEmail");

		Long userId = userService.findOrCreateUserBySocialId("GOOGLE", platformUserId, primaryEmail);

		return tokenService.issueTokens(userId, deviceId);
	}

	public TokenResponseDTO kakaoLogin(String accessToken, String deviceId) {
		Map<String, String> socialUserInfo = kakaoVerifierService.verifyKakaoToken(accessToken);

		Long userId = userService.findOrCreateUserBySocialId(
			socialUserInfo.get("platform"),
			socialUserInfo.get("platformUserId"),
			socialUserInfo.get("primaryEmail")
		);

		return tokenService.issueTokens(userId, deviceId);
	}

	public TokenResponseDTO appleLogin(String idToken, String deviceId) {
		// 1. Apple ID Token 검증 & 사용자 정보 추출
		Map<String, String> socialUserInfo = appleVerifierService.verifyAppleIdToken(idToken);

		// 2. 사용자 ID,이메일 추출
		String platformUserId = socialUserInfo.get("platformUserId");
		String primaryEmail = socialUserInfo.get("primaryEmail");

		// 3. 소셜 ID 기반 사용자 조회 or 생성
		Long userId = userService.findOrCreateUserBySocialId("APPLE", platformUserId, primaryEmail);

		// 4. JWT (Access/Refresh Token) 발급
		log.info("✅ Apple ID Token 검증 & JWT 발급 성공. 테스트용 User ID: {}", userId);
		return tokenService.issueTokens(userId, deviceId);
	}

}
