package org.sopt.pawkey.backendapi.domain.routes.api.controller;

import static org.sopt.pawkey.backendapi.global.constants.AppConstants.*;

import org.sopt.pawkey.backendapi.domain.routes.api.dto.RouteRegisterRequest;
import org.sopt.pawkey.backendapi.domain.routes.application.facade.command.RouteRegisterFacade;
import org.sopt.pawkey.backendapi.global.constants.AppConstants;
import org.sopt.pawkey.backendapi.global.response.ApiResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

	@PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
	@Operation(summary = "산책 루트 정보 등록", description = "산책 루트 정보 등록 API입니다.", tags = {"Route"})
	@ApiResponses({
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "산책 루트 정보 등록 성공"),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "조회 실패 (U40401 또는 R40401 에러코드 확인)")})
	public ResponseEntity<ApiResponse<Void>> registerRoute(@RequestHeader(AppConstants.USER_ID_HEADER) Long userId,
		@RequestPart("trackingImage") MultipartFile trackingImage,
		@Valid @RequestPart("routeRequest") RouteRegisterRequest routeRegisterRequest) {
		return ResponseEntity.ok(
			ApiResponse.success(routeRegisterFacade.execute(userId, routeRegisterRequest.toCommand(), trackingImage)));
	}

}
