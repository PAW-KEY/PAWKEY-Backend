package org.sopt.pawkey.backendapi.domain.routes.api.controller;

import static org.sopt.pawkey.backendapi.global.constants.AppConstants.*;

import org.sopt.pawkey.backendapi.domain.routes.api.dto.GetSharedRouteMapDataResponseDto;
import org.sopt.pawkey.backendapi.domain.routes.api.dto.GetRouteInfoForPostResponse;
import org.sopt.pawkey.backendapi.domain.routes.api.dto.RouteRegisterRequest;
import org.sopt.pawkey.backendapi.domain.routes.application.dto.command.GetSharedRouteMapDataCommandDto;
import org.sopt.pawkey.backendapi.domain.routes.application.dto.result.GetSharedRouteMapDataResultDto;
import org.sopt.pawkey.backendapi.domain.routes.api.dto.RouteRegisterResponse;
import org.sopt.pawkey.backendapi.domain.routes.application.dto.command.GetRouteInfoForPostCommand;
import org.sopt.pawkey.backendapi.domain.routes.application.dto.result.GetRouteInfoForPostResult;
import org.sopt.pawkey.backendapi.domain.routes.application.dto.result.RouteRegisterResult;
import org.sopt.pawkey.backendapi.domain.routes.application.facade.command.RouteRegisterFacade;
import org.sopt.pawkey.backendapi.domain.routes.application.facade.query.GetSharedRouteMapDataFacade;
import org.sopt.pawkey.backendapi.domain.routes.application.facade.query.GetRouteInfoForPostFacade;
import org.sopt.pawkey.backendapi.global.response.ApiResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(API_PREFIX + "/routes")
@RequiredArgsConstructor
public class RouteController {

	private final RouteRegisterFacade routeRegisterFacade;
	private final GetSharedRouteMapDataFacade getSharedRouteMapDataFacade;
	private final GetRouteInfoForPostFacade getRouteInfoForPostFacade;

	@PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
	@Operation(summary = "산책 루트 정보 등록", description = "산책 루트 정보 등록 API입니다.", tags = {"Route"})
	@ApiResponses({
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "산책 루트 정보 등록 성공"),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "조회 실패 (U40401 또는 R40401 에러코드 확인)")})
	public ResponseEntity<ApiResponse<RouteRegisterResponse>> registerRoute(
		@RequestHeader(USER_ID_HEADER) Long userId,
		@RequestPart("trackingImage") MultipartFile trackingImage,
		@Valid @RequestPart("routeRequest") RouteRegisterRequest routeRegisterRequest) {

		RouteRegisterResult result = routeRegisterFacade.execute(
			userId,
			routeRegisterRequest.toCommand(),
			trackingImage);

		return ResponseEntity.ok(
			ApiResponse.success(RouteRegisterResponse.from(result)));
	}

	@GetMapping("/{routeId}/track")
	@Operation(summary = "트래킹 정보 조회", description = "공유된 루트의 좌표 정보 조회 API입니다.", tags = {"Route"})
	@ApiResponses({
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "산책 루트 정보 조회 성공"),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "조회 실패 (U40401 또는 R40401 에러코드 확인)")})
	public ResponseEntity<ApiResponse<GetSharedRouteMapDataResponseDto>> getSharedRouteMapData(
		@RequestHeader(USER_ID_HEADER) Long userId,
		@PathVariable("routeId") Long routeId
	) {
		GetSharedRouteMapDataCommandDto commandDto = new GetSharedRouteMapDataCommandDto(routeId);
		GetSharedRouteMapDataResultDto resultDto = getSharedRouteMapDataFacade.execute(userId, commandDto);
		return ResponseEntity.ok(
			ApiResponse.success(
				GetSharedRouteMapDataResponseDto.from(resultDto)
			)
		);
	}

	@GetMapping("/{routeId}/info")
	@Operation(summary = "산책 루트 정보 조회", description = "게시물 등록 페이지에서 필요한 산책 루트 정보 조회하는 api입니다.", tags = {"Route"})
	@ApiResponses({
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "산책 루트 정보 조회 성공"),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "조회 실패 (U40401, U40402 또는 R40401 에러코드 확인)"),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "권한 검증 실패 (R40301 에러코드 확인)")
	})
	public ResponseEntity<ApiResponse<GetRouteInfoForPostResponse>> getTrackingInfo(
		@RequestHeader(USER_ID_HEADER) Long userId,
		@PathVariable("routeId") Long routeId
	) {

		GetRouteInfoForPostResult result = getRouteInfoForPostFacade.execute(userId,
			new GetRouteInfoForPostCommand(routeId));

		return ResponseEntity.ok(ApiResponse.success(GetRouteInfoForPostResponse.from(result)));
	}
}
