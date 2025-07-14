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
	@Operation(summary = "유저 소속 지역 수정 성공", description = "유저가 소속된 지역(region)을 수정합니다.", tags = {"Posts"})
	@ApiResponses({
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "유저 소속 지역 수정 성공")
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
