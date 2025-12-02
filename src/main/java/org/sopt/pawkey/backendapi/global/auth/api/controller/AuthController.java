package org.sopt.pawkey.backendapi.global.auth.api.controller;

import static org.sopt.pawkey.backendapi.global.constants.AppConstants.*;

import org.sopt.pawkey.backendapi.domain.user.application.facade.UserLoginFacade;
import org.sopt.pawkey.backendapi.global.auth.api.dto.request.LoginRequestDTO;
import org.sopt.pawkey.backendapi.global.auth.api.dto.request.RefreshTokenRequestDTO;
import org.sopt.pawkey.backendapi.global.auth.api.dto.response.TokenResponseDTO;
import org.sopt.pawkey.backendapi.global.auth.application.service.TokenService;
import org.sopt.pawkey.backendapi.global.auth.application.verifier.KakaoAuthService;
import org.springframework.http.ResponseEntity;
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
	public TokenResponseDTO googleLogin(@RequestBody @Valid LoginRequestDTO request) {
		// Facade로 ID Token과 Device ID를 전달하여 모든 인증 및 토큰 발급 로직을 처리
		return userLoginFacade.googleLogin(request.idToken(), request.deviceId());
	}

	// 2. 토큰 갱신 API
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
		// TokenService.rotate 호출 시, RefreshTokenRequestDTO의 필드명에 맞게 호출해야 합니다.
		// RefreshTokenRequestDTO가 record이므로 필드 접근은 메서드 형태로 (request.refreshToken(), request.deviceId())
		TokenResponseDTO response = tokenService.rotate(request.refreshToken(), request.deviceId());
		// ApiResponse 유틸리티를 사용한다고 가정하고 코드를 작성합니다.
		// return ResponseEntity.ok(ApiResponse.success(response));
		return ResponseEntity.ok(response);
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
	public TokenResponseDTO kakaoLogin(@RequestBody @Valid LoginRequestDTO request) {
		// 카카오는 idToken 대신 Access Token을 받으므로 request.idToken() -> accessToken 개념임
		return userLoginFacade.kakaoLogin(request.idToken(), request.deviceId());
	}

	@GetMapping("/kakao/callback") //서버 테스트용 임시 컨트롤러
	public ResponseEntity<?> kakaoCallback(@RequestParam String code) {
		String accessToken = kakaoAuthService.exchangeCodeForAccessToken(code);
		String testDeviceId = "WEB_KAKAO_LOGIN";
		TokenResponseDTO tokens = userLoginFacade.kakaoLogin(accessToken, testDeviceId);
		return ResponseEntity.ok(tokens);

	}



}
