package org.sopt.pawkey.backendapi.domain.user.application.facade;

import java.util.Map;

import org.sopt.pawkey.backendapi.domain.auth.api.dto.response.SocialLoginResponseDTO;
import org.sopt.pawkey.backendapi.domain.auth.api.dto.response.TokenResponseDTO;
import org.sopt.pawkey.backendapi.domain.auth.application.service.login.verifier.apple.AppleTokenVerifier;
import org.sopt.pawkey.backendapi.domain.auth.application.service.login.verifier.google.GoogleTokenVerifier;
import org.sopt.pawkey.backendapi.domain.auth.application.service.login.verifier.kakao.KakaoTokenVerifier;
import org.sopt.pawkey.backendapi.domain.auth.application.service.token.AppleRefreshTokenService;
import org.sopt.pawkey.backendapi.domain.auth.application.service.token.TokenService;
import org.sopt.pawkey.backendapi.domain.auth.domain.Provider;
import org.sopt.pawkey.backendapi.domain.auth.exception.AuthBusinessException;
import org.sopt.pawkey.backendapi.domain.auth.exception.AuthErrorCode;
import org.sopt.pawkey.backendapi.domain.user.api.dto.result.UserCreationResult;
import org.sopt.pawkey.backendapi.domain.user.application.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserLoginFacade {

	private final UserService userService;
	private final TokenService tokenService;
	private final GoogleTokenVerifier googleVerifierService;
	private final KakaoTokenVerifier kakaoVerifierService;
	private final AppleTokenVerifier appleVerifierService;
	private final AppleRefreshTokenService appleRefreshTokenService;
	public SocialLoginResponseDTO googleLogin(String idToken, String deviceId) {
		Map<String, String> socialUserInfo = googleVerifierService.verifyGoogleToken(idToken);

		String platformUserId = socialUserInfo.get("platformUserId");
		String primaryEmail = socialUserInfo.get("primaryEmail");

		UserCreationResult creationResult = userService.findOrCreateUserBySocialId(Provider.GOOGLE, platformUserId, primaryEmail);
		TokenResponseDTO tokens = tokenService.issueTokens(creationResult.userId(), deviceId);
		return SocialLoginResponseDTO.of(tokens, creationResult.isNewUser());
	}

	public SocialLoginResponseDTO kakaoLogin(String accessToken, String deviceId) {
		Map<String, String> socialUserInfo = kakaoVerifierService.verifyKakaoToken(accessToken);

		UserCreationResult creationResult = userService.findOrCreateUserBySocialId(
			Provider.KAKAO,
			socialUserInfo.get("platformUserId"),
			socialUserInfo.get("primaryEmail")
		);

		TokenResponseDTO tokens = tokenService.issueTokens(creationResult.userId(), deviceId);
		return SocialLoginResponseDTO.of(tokens, creationResult.isNewUser());

	}

	@Transactional
	public SocialLoginResponseDTO appleLoginWithCode(String authorizationCode, String deviceId) { //변경
		//code → Apple token
		Map<String, Object> tokenResponse =
			appleVerifierService.exchangeCodeForTokens(authorizationCode);

		String idToken = (String) tokenResponse.get("id_token");
		String refreshToken = (String) tokenResponse.get("refresh_token");

		if (idToken == null) {
			throw new AuthBusinessException(AuthErrorCode.SOCIAL_LOGIN_FAIL);
		}

		// idToken 검증
		Map<String, String> info =
			appleVerifierService.verifyAppleIdToken(idToken);

		UserCreationResult result =
			userService.findOrCreateUserBySocialId(
				Provider.APPLE,
				info.get("platformUserId"),
				info.get("primaryEmail")
			);

		// refresh_token 저장 (탈퇴 대비)
		if (refreshToken != null) {
			appleRefreshTokenService.saveOrUpdate(result.userId(), refreshToken);
		}

		//JWT 발급
		return SocialLoginResponseDTO.of(
			tokenService.issueTokens(result.userId(), deviceId),
			result.isNewUser()
		);
	}

}
