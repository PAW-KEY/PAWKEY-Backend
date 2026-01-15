package org.sopt.pawkey.backendapi.domain.walkPreparation.api.controller;

import static org.sopt.pawkey.backendapi.global.constants.AppConstants.*;

import org.sopt.pawkey.backendapi.domain.auth.annotation.UserId;
import org.sopt.pawkey.backendapi.domain.routes.application.facade.command.RouteStartFacade;
import org.sopt.pawkey.backendapi.domain.walkPreparation.api.dto.request.WalkPreparationRequestDto;
import org.sopt.pawkey.backendapi.domain.walkPreparation.api.dto.response.WalkPreparationResponseDto;
import org.sopt.pawkey.backendapi.domain.walkPreparation.application.facade.PreparationFacade;
import org.sopt.pawkey.backendapi.domain.weather.api.dto.WeatherMessageResponseDTO;
import org.sopt.pawkey.backendapi.global.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(API_PREFIX + "/walk/preparation")
public class WalkPreparationController {

	private final RouteStartFacade routeStartFacade;

	@Operation(
		summary = "산책 안내 메시지",
		description = "산책 시작 전, 현재 날씨에 맞는 멘트를 제공합니다.",
		tags = {"WalkPreparation"}
	)
	@ApiResponses({
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "멘트 조회 성공"),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "날씨 정보 또는 유저 지역 정보를 찾을 수 없음"),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 내부 오류")
	})
	@GetMapping("/message")
	public ResponseEntity<ApiResponse<WeatherMessageResponseDTO>> getStartMessage(
		@Parameter(hidden = true) @UserId Long userId
	) {
		WeatherMessageResponseDTO response = routeStartFacade.getReadyData(userId);
		return ResponseEntity.ok(ApiResponse.success(response));
	}

	private final PreparationFacade preparationFacade;

	@Operation(summary = "산책 준비물 조회", tags = {"WalkPreparation"})
	@ApiResponses({
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공")
	})
	@GetMapping
	public ResponseEntity<ApiResponse<WalkPreparationResponseDto>> getPreparation(
		@Parameter(hidden = true) @UserId Long userId
	) {
		return ResponseEntity.ok(ApiResponse.success(preparationFacade.getPreparation(userId)));
	}

	@Operation(summary = "산책 준비물 저장(동기화)", tags = {"WalkPreparation"})
	@ApiResponses({
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "저장 성공")
	})
	@PatchMapping
	public ResponseEntity<ApiResponse<WalkPreparationResponseDto>> updatePreparation(
		@Parameter(hidden = true) @UserId Long userId,
		@RequestBody @Valid WalkPreparationRequestDto request
	) {
		return ResponseEntity.ok(ApiResponse.success(preparationFacade.updatePreparation(userId, request)));
	}
}