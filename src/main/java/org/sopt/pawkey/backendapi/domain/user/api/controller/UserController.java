package org.sopt.pawkey.backendapi.domain.user.api.controller;

import static org.sopt.pawkey.backendapi.global.constants.AppConstants.*;

import java.util.List;

import org.sopt.pawkey.backendapi.domain.auth.annotation.UserId;
import org.sopt.pawkey.backendapi.domain.pet.api.dto.response.PetProfileResponseDto;
import org.sopt.pawkey.backendapi.domain.post.api.dto.response.PostCardResponseDto;
import org.sopt.pawkey.backendapi.domain.user.api.dto.request.CreateUserRequestDto;
import org.sopt.pawkey.backendapi.domain.user.api.dto.request.UpdateUserRegionRequestDto;
import org.sopt.pawkey.backendapi.domain.user.api.dto.response.ListResponseWrapper;
import org.sopt.pawkey.backendapi.domain.user.api.dto.response.UserInfoResponseDto;
import org.sopt.pawkey.backendapi.domain.user.api.dto.response.UserRegisterResponseDto;
import org.sopt.pawkey.backendapi.domain.user.application.dto.request.UserRegisterCommand;
import org.sopt.pawkey.backendapi.domain.user.application.facade.UserRegisterFacade;
import org.sopt.pawkey.backendapi.domain.user.application.facade.command.UpdateUserRegionFacade;
import org.sopt.pawkey.backendapi.domain.user.application.facade.query.UserLikedPostQueryFacade;
import org.sopt.pawkey.backendapi.domain.user.application.facade.query.UserPetQueryFacade;
import org.sopt.pawkey.backendapi.domain.user.application.facade.query.UserQueryFacade;
import org.sopt.pawkey.backendapi.domain.user.application.facade.query.UserWrittenPostQueryFacade;
import org.sopt.pawkey.backendapi.global.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(API_PREFIX + "/users")
public class UserController {

	private final UserPetQueryFacade userPetQueryFacade;
	private final UpdateUserRegionFacade updateUserRegionFacade;
	private final UserLikedPostQueryFacade userLikedPostQueryFacade;
	private final UserWrittenPostQueryFacade userWrittenPostQueryFacade;
	private final UserRegisterFacade userRegisterFacade;
	private final UserQueryFacade userQueryFacade;

	@Operation(summary = "유저 정보 등록", description = "회원가입과 동시에, 유저 정보 등록. ", tags = {"Users"})
	@ApiResponses({
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "유저 정보 등록 성공"),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "중복된 로그인 아이디입니다.", content = @Content(mediaType = "application/json")),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "해당 지역 정보를 찾을 수 없습니다.", content = @Content(mediaType = "application/json")),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "유효하지 않은 강아지 성향 카테고리 선택입니다.", content = @Content(mediaType = "application/json")),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "강아지 프로필 이미지 파일 형식 또는 용량 오류", content = @Content(mediaType = "application/json")),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @Content(mediaType = "application/json"))})
	@PostMapping
	public ResponseEntity<ApiResponse<UserRegisterResponseDto>> createUser(
		@AuthenticationPrincipal Long userId,
		@RequestBody @Valid CreateUserRequestDto requestDto) {

		UserRegisterCommand command = requestDto.toCommand();
		UserRegisterResponseDto response = userRegisterFacade.execute(userId, command);

		return ResponseEntity.ok(ApiResponse.success(response));
	}

	@Operation(summary = "내가 좋아요한 게시물 조회", description = "사용자가 좋아요를 누른 게시물 목록을 반환합니다.", tags = {"Users"})
	@ApiResponses({
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공"),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "사용자 없음"),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 오류")
	})
	@GetMapping("/me/likes")
	public ResponseEntity<ApiResponse<ListResponseWrapper<PostCardResponseDto>>> getMyLikedPosts(
		@UserId Long userId
	) {
		List<PostCardResponseDto> likedPosts = userLikedPostQueryFacade.getLikedPosts(userId);
		return ResponseEntity.ok(ApiResponse.success(ListResponseWrapper.from(likedPosts)));
	}

	@Operation(summary = "마이페이지 유저 정보 조회", description = "마이페이지에서 유저 상세 정보를 조회합니다.", tags = {"Users"})
	@ApiResponses({
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공"),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "사용자 없음"),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 오류")
	})
	@GetMapping("/me/userInfo")
	public ResponseEntity<ApiResponse<UserInfoResponseDto>> getUserProfile(
		@UserId Long userId
	) {
		UserInfoResponseDto response = userQueryFacade.getUserInfo(userId);
		return ResponseEntity.ok(ApiResponse.success(response));
	}

	@Operation(summary = "내가 작성한 게시물 조회", description = "사용자가 작성한 게시물 목록을 반환합니다.", tags = {"Users"})
	@ApiResponses({
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공"),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "사용자 없음"),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 오류")
	})
	@GetMapping("/me/posts")
	public ResponseEntity<ApiResponse<ListResponseWrapper<PostCardResponseDto>>> getMyPosts(
		@UserId Long userId
	) {
		List<PostCardResponseDto> myPosts = userWrittenPostQueryFacade.getMyPosts(userId);
		return ResponseEntity.ok(ApiResponse.success(ListResponseWrapper.from(myPosts)));
	}

	@Operation(summary = "유저 반려견 프로필 조회", description = "유저가 등록한 반려견 목록을 조회합니다.", tags = {"Users"})
	@ApiResponses({
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공"),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "사용자 없음"),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 오류")
	})
	@GetMapping("/me/pets")
	public ResponseEntity<ApiResponse<List<PetProfileResponseDto>>> getMyPets(
		@UserId Long userId
	) {
		List<PetProfileResponseDto> petDtos = userPetQueryFacade.getUserPets(userId);
		return ResponseEntity.ok(ApiResponse.success(petDtos));
	}

	@PatchMapping("/me/regions")
	@Operation(summary = "유저 소속 지역 수정 성공", description = "유저가 소속된 지역(region)을 수정합니다.", tags = {"Users"})
	@ApiResponses({
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "유저 소속 지역 수정 성공"),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "유효하지 않은 요청 (R40001: 지역구분이 동이 아닌 경우)"),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "리소스 찾을 수 없음(U40401, R40401)")
	})
	public ResponseEntity<ApiResponse<Void>> updateRegion(
		@UserId Long userId,
		@Valid @RequestBody UpdateUserRegionRequestDto updateUserRegionRequestDto
	) {
		updateUserRegionFacade.execute(userId, updateUserRegionRequestDto.toCommand());

		return ResponseEntity.ok(
			ApiResponse.success());
	}
}


