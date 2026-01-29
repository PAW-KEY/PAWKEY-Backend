package org.sopt.pawkey.backendapi.domain.home.api.controller;

import static org.sopt.pawkey.backendapi.global.constants.AppConstants.*;

import org.sopt.pawkey.backendapi.domain.auth.annotation.UserId;
import org.sopt.pawkey.backendapi.domain.home.api.dto.response.HomeInfoResponseDto;
import org.sopt.pawkey.backendapi.domain.home.application.facade.HomeQueryFacade;
import org.sopt.pawkey.backendapi.global.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(API_PREFIX + "/home")
public class HomeController {

	private final HomeQueryFacade homeQueryFacade;

	@Operation(summary = "홈 산책 정보 조회", description = "유저의 이번 달 누적 산책 정보를 조회합니다.", tags = {"Home"})
	@ApiResponses({
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공"),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "사용자 정보 없음"),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 오류")
	})
	@GetMapping("/info")
	public ResponseEntity<ApiResponse<HomeInfoResponseDto>> getHomeInfo(
		@Parameter(hidden = true) @UserId Long userId
	) {
		HomeInfoResponseDto response = homeQueryFacade.getHomeInfo(userId);
		return ResponseEntity.ok(ApiResponse.success(response));
	}
}