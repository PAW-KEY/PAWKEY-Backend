package org.sopt.pawkey.backendapi.domain.routes.api.controller;

import static org.sopt.pawkey.backendapi.global.constants.AppConstants.*;

import org.sopt.pawkey.backendapi.domain.auth.annotation.UserId;
import org.sopt.pawkey.backendapi.domain.routes.api.dto.request.SaveRouteRequestDTO;
import org.sopt.pawkey.backendapi.domain.routes.api.dto.response.*;
import org.sopt.pawkey.backendapi.domain.routes.api.dto.request.RouteRegisterRequest;
import org.sopt.pawkey.backendapi.domain.routes.application.dto.command.GetRouteGeometryCommand;
import org.sopt.pawkey.backendapi.domain.routes.application.dto.command.GetRouteInfoForPostCommand;
import org.sopt.pawkey.backendapi.domain.routes.application.dto.command.GetRouteSummaryCommand;
import org.sopt.pawkey.backendapi.domain.routes.application.dto.command.GetSharedRouteMapDataCommandDto;
import org.sopt.pawkey.backendapi.domain.routes.application.dto.result.*;
import org.sopt.pawkey.backendapi.domain.routes.application.facade.command.RouteRegisterFacade;
import org.sopt.pawkey.backendapi.domain.routes.application.facade.query.GetRouteGeometryFacade;
import org.sopt.pawkey.backendapi.domain.routes.application.facade.query.GetRouteInfoForPostFacade;
import org.sopt.pawkey.backendapi.domain.routes.application.facade.query.GetRouteSummaryFacade;
import org.sopt.pawkey.backendapi.domain.routes.application.facade.query.GetSharedRouteMapDataFacade;
import org.sopt.pawkey.backendapi.domain.routes.recommendation.application.dto.result.RecommendationResult;
import org.sopt.pawkey.backendapi.domain.routes.recommendation.application.facade.RecommendationFacade;
import org.sopt.pawkey.backendapi.global.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequestMapping(API_PREFIX + "/routes")
@RequiredArgsConstructor
public class RouteController {

	private final RouteRegisterFacade routeRegisterFacade;
	private final GetRouteGeometryFacade getRouteGeometryFacade;
	private final GetRouteSummaryFacade getRouteSummaryFacade;
	private final RecommendationFacade recommendationFacade;


	@GetMapping("/{routeId}/geometry")
	@Operation(summary = "산책 경로 좌표 조회" , description = "산책 경로 좌표(LineString) 조회 API", tags = {"Route"})
	public ResponseEntity<ApiResponse<GetRouteGeometryResponseDto>> getRouteGeometry(
			@Parameter(hidden = true) @UserId Long userId,
			@PathVariable("routeId") Long routeId)
	{
		GetRouteGeometryCommand command = new GetRouteGeometryCommand(routeId);
		GetRouteGeometryResult result = getRouteGeometryFacade.execute(userId, command);

		return ResponseEntity.ok(ApiResponse.success(GetRouteGeometryResponseDto.from(result)));
	}

	@GetMapping("/{routeId}/summary")
	@Operation(summary = "산책 루트 요약 정보 조회", description = "산책 카드용 요약 정보 조회 API", tags = {"Route"})
	public ResponseEntity<ApiResponse<GetRouteSummaryResponseDto>> getRouteSummary(
			@Parameter(hidden = true) @UserId Long userId,
			@PathVariable("routeId") Long routeId
	) {
		GetRouteSummaryCommand commandDto = new GetRouteSummaryCommand(routeId);
		GetRouteSummaryResult resultDto = getRouteSummaryFacade.execute(userId, commandDto);

		return ResponseEntity.ok(
				ApiResponse.success(GetRouteSummaryResponseDto.from(resultDto))
		);
	}


	@PostMapping("/{routeId}/finish")
	@Operation(summary = "산책 종료", description = "산책을 종료하고 루트를 저장합니다.", tags = {"Route"})
	public ResponseEntity<ApiResponse<SaveRouteResponseDTO>> finishWalk(
			@Parameter(hidden = true) @UserId Long userId,
			@PathVariable String routeId,
			@RequestBody @Valid SaveRouteRequestDTO request
	) {
		FinishWalkResult result =
				routeRegisterFacade.finishWalk(userId, request.toCommand(userId,routeId));

		return ResponseEntity.ok(
				ApiResponse.success(SaveRouteResponseDTO.from(result))
		);
	}


	@GetMapping("/recommendation")
	@Operation(summary = "맞춤 루트 추천 조회", description = "나와 비슷한 성향 유저의 인기 루트 4개를 추천합니다.", tags = {"Home"})
	public ResponseEntity<ApiResponse<RecommendationResponseDTO>> getRecommendations(
			@Parameter(hidden = true) @UserId Long userId
	) {
		List<RecommendationResult> results = recommendationFacade.getHomeRecommendations(userId);

		RecommendationResponseDTO response = RecommendationResponseDTO.from(results);

		return ResponseEntity.ok(
				ApiResponse.success(response)
		);
	}


}
