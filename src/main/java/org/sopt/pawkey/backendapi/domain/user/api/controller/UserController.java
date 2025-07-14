package org.sopt.pawkey.backendapi.domain.user.api.controller;

import static org.sopt.pawkey.backendapi.global.constants.AppConstants.*;

import org.sopt.pawkey.backendapi.domain.user.api.dto.request.CreateUserRequestDto;
import org.sopt.pawkey.backendapi.domain.user.api.dto.response.UserRegisterResponseDto;
import org.sopt.pawkey.backendapi.domain.user.application.dto.request.UserRegisterCommand;
import org.sopt.pawkey.backendapi.domain.user.application.facade.UserRegisterFacade;
import org.sopt.pawkey.backendapi.global.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(API_PREFIX + "/users")

public class UserController {

	private final UserRegisterFacade userRegisterFacade;

	@Operation(summary = "유저 정보 등록", description = "회원가입과 동시에, 유저 정보 등록. ", tags = {"Users"})
	@ApiResponses({
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "유저 정보 등록 성공"),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "중복된 로그인 아이디입니다.", content = @Content(mediaType = "application/json")),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "해당 지역 정보를 찾을 수 없습니다.", content = @Content(mediaType = "application/json")),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "유효하지 않은 강아지 성향 카테고리 선택입니다.", content = @Content(mediaType = "application/json")),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "강아지 프로필 이미지 파일 형식 또는 용량 오류", content = @Content(mediaType = "application/json")),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @Content(mediaType = "application/json"))})

	@PostMapping("")
	public ResponseEntity<ApiResponse<UserRegisterResponseDto>> createUser(
		@RequestHeader(USER_ID_HEADER) @NotNull Integer userId,
		@RequestPart("data") @Valid @NotNull CreateUserRequestDto requestDto,

		@RequestPart("pet_profile") @Valid @NotNull MultipartFile image) {
		UserRegisterCommand command = requestDto.toCommand();
		UserRegisterResponseDto response = userRegisterFacade.execute(command, image);

		return ResponseEntity.ok(ApiResponse.success(response));

	}
}
