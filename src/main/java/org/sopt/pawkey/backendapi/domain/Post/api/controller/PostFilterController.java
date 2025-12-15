package org.sopt.pawkey.backendapi.domain.post.api.controller;

import static org.sopt.pawkey.backendapi.global.constants.AppConstants.*;

import org.sopt.pawkey.backendapi.domain.category.api.dto.response.CategorySelectListResponseDto;
import org.sopt.pawkey.backendapi.domain.category.application.facade.query.CategorySelectFacade;
import org.sopt.pawkey.backendapi.domain.post.api.dto.request.FilterPostsRequestDto;
import org.sopt.pawkey.backendapi.domain.post.api.dto.response.PostListResponseDto;
import org.sopt.pawkey.backendapi.domain.post.application.facade.query.PostQueryFacade;
import org.sopt.pawkey.backendapi.domain.auth.annotation.UserId;
import org.sopt.pawkey.backendapi.global.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(API_PREFIX + "/posts/filter")
public class PostFilterController {

	private final CategorySelectFacade categorySelectFacade;
	private final PostQueryFacade postQueryFacade;

	@Operation(summary = "필터링 카테고리 리스트 조회", description = "게사물 필터링에서 필요한 카테고리 정보를 리스트 형식으로 조회합니다", tags = {"Posts"})
	@ApiResponses({
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "카테고리 조회 성공"),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(mediaType = "application/json")),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @Content(mediaType = "application/json"))})
	@GetMapping("")
	public ResponseEntity<ApiResponse<CategorySelectListResponseDto>> getCategories(
		@RequestHeader(USER_ID_HEADER) @NotNull Integer userId) {
		CategorySelectListResponseDto response = categorySelectFacade.getFilterOptions();
		return ResponseEntity.ok(ApiResponse.success(response));
	}

	@Operation(summary = "게시물 필터링 조회", description = "필터링한 산책 게시물의 리스트를 조회합니다.", tags = {"Posts"})
	@ApiResponses({
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "게시물 필터 조회 성공"),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(mediaType = "application/json")),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @Content(mediaType = "application/json"))})
	@PostMapping("")
	public ResponseEntity<ApiResponse<PostListResponseDto>> filterPosts(
		@UserId Long userId,
		@RequestBody @Valid @NotNull FilterPostsRequestDto requestDto) {
		PostListResponseDto response = postQueryFacade.getFilterPostList(requestDto, Long.valueOf(userId));
		return ResponseEntity.ok(ApiResponse.success(response));
	}
}