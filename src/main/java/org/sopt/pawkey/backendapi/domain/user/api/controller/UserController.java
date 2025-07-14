package org.sopt.pawkey.backendapi.domain.user.api.controller;

import static org.sopt.pawkey.backendapi.global.constants.AppConstants.*;

import org.sopt.pawkey.backendapi.domain.user.api.dto.request.UpdateUserRegionRequestDto;
import org.sopt.pawkey.backendapi.domain.user.application.facade.command.UpdateUserRegionFacade;
import org.sopt.pawkey.backendapi.global.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(API_PREFIX + "/users")
public class UserController {

	private final UpdateUserRegionFacade updateUserRegionFacade;

	@PatchMapping("/me/regions")
	@Operation(summary = "유저 소속 지역 수정 성공", description = "유저가 소속된 지역(region)을 수정합니다.", tags = {"Users"})
	@ApiResponses({
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "유저 소속 지역 수정 성공"),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "유효하지 않은 요청 (R40001: 지역구분이 동이 아닌 경우)"),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "리소스 찾을 수 없음(U40401, R40401)")
	})

	public ResponseEntity<ApiResponse<Void>> updateRegion(
		@RequestHeader(USER_ID_HEADER) @NotNull Long userId,
		@Valid @RequestBody UpdateUserRegionRequestDto updateUserRegionRequestDto
	) {
		updateUserRegionFacade.execute(userId, updateUserRegionRequestDto.toCommand());

		return ResponseEntity.ok(
			ApiResponse.success());
	}
}
