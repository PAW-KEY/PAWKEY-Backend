package org.sopt.pawkey.backendapi.domain.user.api.controller;

import static org.sopt.pawkey.backendapi.global.constants.AppConstants.*;

import java.util.List;

import org.sopt.pawkey.backendapi.domain.user.api.dto.response.LikedPostResponseDto;
import org.sopt.pawkey.backendapi.domain.user.api.dto.response.ListResponseWrapper;
import org.sopt.pawkey.backendapi.domain.user.application.facade.query.UserLikedPostQueryFacade;
import org.sopt.pawkey.backendapi.global.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(API_PREFIX + "/users")
public class UserController {

	private final UserLikedPostQueryFacade userLikedPostQueryFacade;

	@Operation(summary = "내가 좋아요한 게시물 조회", description = "사용자가 좋아요를 누른 게시물 목록을 반환합니다.", tags = {"Users"})
	@ApiResponses({
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공"),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "사용자 없음"),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 오류")
	})
	@GetMapping("/me/likes")
	public ResponseEntity<ApiResponse<ListResponseWrapper<LikedPostResponseDto>>> getMyLikedPosts(
		@RequestHeader("X-USER-ID") Long userId
	) {
		List<LikedPostResponseDto> likedPosts = userLikedPostQueryFacade.getLikedPosts(userId);
		return ResponseEntity.ok(ApiResponse.success(ListResponseWrapper.from(likedPosts)));
	}
}
