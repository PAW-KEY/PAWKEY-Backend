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

		Map<String, String> socialUserInfo = googleVerifierService.verifyGoogleToken(idToken);



		String platformUserId = "dummyGoogleId12345";
		String primaryEmail = "dummy@google.com";


		Long userId = userService.findOrCreateUserBySocialId("GOOGLE", platformUserId, primaryEmail);


		return tokenService.issueTokens(userId, deviceId);
	}

}
