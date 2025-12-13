package org.sopt.pawkey.backendapi.global.auth.api.controller;

import static org.sopt.pawkey.backendapi.global.constants.AppConstants.*;

import org.sopt.pawkey.backendapi.domain.user.application.facade.UserLoginFacade;
import org.sopt.pawkey.backendapi.global.auth.annotation.UserId;
import org.sopt.pawkey.backendapi.global.auth.api.dto.request.LoginRequestDTO;
import org.sopt.pawkey.backendapi.global.auth.api.dto.request.LogoutRequestDTO;
import org.sopt.pawkey.backendapi.global.auth.api.dto.request.RefreshTokenRequestDTO;
import org.sopt.pawkey.backendapi.global.auth.api.dto.request.WithdrawRequestDTO;
import org.sopt.pawkey.backendapi.global.auth.api.dto.response.SocialLoginResponseDTO;
import org.sopt.pawkey.backendapi.global.auth.api.dto.response.TokenResponseDTO;
import org.sopt.pawkey.backendapi.global.auth.application.service.TokenService;
import org.sopt.pawkey.backendapi.global.auth.application.verifier.KakaoAuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(API_PREFIX + "/auth")
public class AuthController {

	private final UserLoginFacade userLoginFacade;
	private final TokenService tokenService;
	private final KakaoAuthService kakaoAuthService;

	// 1. 소셜 로그인 API
	@Operation(summary = "Google 소셜 로그인", description = "ID Token을 받아 사용자 인증 및 Access/Refresh Token을 최초 발급합니다.", tags = {
		"Auth"})
	@ApiResponses({
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "최초 토큰 발급 성공"),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "유효하지 않은 요청 데이터 (ID Token 또는 Device ID 누락)", content = @Content(mediaType = "application/json")),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "소셜 토큰 검증 실패 (A40106)", content = @Content(mediaType = "application/json")),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @Content(mediaType = "application/json"))
	})
	@PostMapping("/google/login")
	public SocialLoginResponseDTO googleLogin(@RequestBody @Valid LoginRequestDTO request) {
		// Facade로 ID Token과 Device ID를 전달하여 모든 인증 및 토큰 발급 로직을 처리
		return userLoginFacade.googleLogin(request.idToken(), request.deviceId());
	}




	// 3. 카카오 로그인 API
	@Operation(summary = "Kakao 소셜 로그인", description = "Access Token을 받아 사용자 인증 및 Access/Refresh Token을 최초 발급합니다.", tags = {
		"Auth"})
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "최초 토큰 발급 성공"),
		@ApiResponse(responseCode = "400", description = "유효하지 않은 요청 데이터 (Access Token 또는 Device ID 누락)", content = @Content(mediaType = "application/json")),
		@ApiResponse(responseCode = "401", description = "소셜 토큰 검증 실패 (A40106)", content = @Content(mediaType = "application/json")),
		@ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @Content(mediaType = "application/json"))
	})
	@PostMapping("/kakao/login")
	public SocialLoginResponseDTO kakaoLogin(@RequestBody @Valid LoginRequestDTO request) {
		// 카카오는 idToken 대신 Access Token을 받으므로 request.idToken() -> accessToken 개념임
		return userLoginFacade.kakaoLogin(request.idToken(), request.deviceId());
	}

	@GetMapping("/kakao/callback") //서버 테스트용 임시 컨트롤러
	public ResponseEntity<?> kakaoCallback(@RequestParam String code) {
		String accessToken = kakaoAuthService.exchangeCodeForAccessToken(code);
		String testDeviceId = "WEB_KAKAO_LOGIN";
		SocialLoginResponseDTO tokens = userLoginFacade.kakaoLogin(accessToken, testDeviceId);
		return ResponseEntity.ok(tokens);

	}

	// Apple 소셜로그인
	@Operation(summary = "Apple 소셜 로그인", description = "ID Token을 받아 사용자 인증 및 Access/Refresh Token을 최초 발급합니다.", tags = {
		"Auth"})
	@ApiResponses({
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "최초 토큰 발급 성공"),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "유효하지 않은 요청 데이터 (ID Token 또는 Device ID 누락)", content = @Content(mediaType = "application/json")),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "소셜 토큰 검증 실패 (A40106)", content = @Content(mediaType = "application/json")),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @Content(mediaType = "application/json"))
	})
	@PostMapping("/apple/login")
	public SocialLoginResponseDTO appleLogin(@RequestBody @Valid LoginRequestDTO request) {
		// Apple 로그인 로직을 UserLoginFacade로 위임
		return userLoginFacade.appleLogin(request.idToken(), request.deviceId());
	}


	@Operation(summary = "토큰 재발급", description = "Refresh Token과 Device ID를 사용하여 새로운 Access/Refresh Token 쌍을 발급합니다. (토큰 로테이션)", tags = {
		"Auth"})
	@ApiResponses({
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "토큰 재발급 성공"),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "유효하지 않은 요청 데이터 (Token 또는 Device ID 누락)", content = @Content(mediaType = "application/json")),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "토큰 무효 (A40102: 만료/유효하지 않음, A40107: 기기 불일치)", content = @Content(mediaType = "application/json")),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @Content(mediaType = "application/json"))
	})
	@PostMapping("/refresh")
	public ResponseEntity<TokenResponseDTO> refreshToken(@RequestBody @Valid RefreshTokenRequestDTO request) {
		TokenResponseDTO response = tokenService.rotate(request.refreshToken(), request.deviceId());
		return ResponseEntity.ok(response);
	}

	@Operation(summary = "로그아웃", description = "Access Token에서 사용자 ID를 추출하고, Refresh Token을 Redis에서 삭제하여 현재 기기의 세션을 무효화합니다. 성공 시 204 No Content를 반환합니다.", tags = {
		"Auth"})
	@ApiResponses({
		@ApiResponse(responseCode = "204", description = "로그아웃 성공 (No Content)"),
		@ApiResponse(responseCode = "400", description = "유효하지 않은 요청 데이터 (Device ID 누락)", content = @Content(mediaType = "application/json")),
		@ApiResponse(responseCode = "401", description = "Access Token 무효/만료", content = @Content(mediaType = "application/json")),
		@ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @Content(mediaType = "application/json"))
	})
	@PostMapping("/logout")
	public ResponseEntity<Void> logout(@UserId Long userId,@RequestBody @Valid LogoutRequestDTO request){
		tokenService.revoke(userId, request.deviceId());
		return ResponseEntity.noContent().build();
	}


	@DeleteMapping("/withdraw")
	public ResponseEntity<Void> withdraw(@UserId Long userId, @RequestBody @Valid WithdrawRequestDTO request){
		authService.withdrawUser(userId, request);
		return ResponseEntity.noContent().build();
	}


}
