package org.sopt.pawkey.backendapi.domain.user.api.controller;

import static org.sopt.pawkey.backendapi.global.constants.AppConstants.*;

import java.util.List;

import org.sopt.pawkey.backendapi.domain.user.api.dto.response.LikedPostResponseDto;
import org.sopt.pawkey.backendapi.domain.user.application.facade.query.UserLikedPostQueryFacade;
import org.sopt.pawkey.backendapi.global.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(API_PREFIX + "/users")
public class UserController {

	// private final UserRegisterFacade userRegisterFacade;
	//
	// @Operation(summary = "유저 생성", description = "회원가입 또는 유저 등록 API입니다.", tags = {"User"})
	// @ApiResponses({
	// 	@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "유저 생성 성공"),
	// 	@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(mediaType = "application/json")),
	// 	@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @Content(mediaType = "application/json"))})
	// @PostMapping
	// public ResponseEntity<ApiResponse<CreateUserResponseDto>> create(
	// 	@RequestBody @Valid CreateUserRequestDto createUserRequestDto) {
	//
	// 	final CreateUserResponseDto response = CreateUserResponseDto.from(
	// 		userRegisterFacade.execute(createUserRequestDto.toCommand()));
	//
	// 	return ResponseEntity.ok(ApiResponse.success(response));
	// }

	private final UserLikedPostQueryFacade userLikedPostQueryFacade;

	@GetMapping("/me/likes")
	public ResponseEntity<ApiResponse<List<LikedPostResponseDto>>> getMyLikedPosts(
		@RequestHeader("X-USER-ID") Long userId
	) {
		List<LikedPostResponseDto> likedPosts = userLikedPostQueryFacade.getLikedPosts(userId);
		return ResponseEntity.ok(ApiResponse.success(likedPosts));
	}
}
