package org.sopt.pawkey.backendapi.domain.post.api.controller;

import static org.sopt.pawkey.backendapi.global.constants.AppConstants.*;

import org.sopt.pawkey.backendapi.domain.auth.annotation.UserId;
import org.sopt.pawkey.backendapi.domain.post.api.dto.request.FilterPostsRequestDto;
import org.sopt.pawkey.backendapi.domain.post.api.dto.response.PostPagingResponseDto;
import org.sopt.pawkey.backendapi.domain.post.application.facade.query.PostQueryFacade;
import org.sopt.pawkey.backendapi.global.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(API_PREFIX + "/posts/filter")
public class PostFilterController {

	private final PostQueryFacade postQueryFacade;

	@Operation(summary = "게시물 필터링 조회", description = "필터링한 산책 게시물의 리스트를 조회합니다.", tags = {"Posts"})
	@ApiResponses({
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "게시물 필터 조회 성공"),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(mediaType = "application/json")),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @Content(mediaType = "application/json"))})
	@PostMapping("")
	public ResponseEntity<ApiResponse<PostPagingResponseDto>> filterPosts(
			@Parameter(hidden = true) @UserId Long userId,
			@RequestParam(defaultValue = "latest") String sortBy,
			@RequestParam(required = false) String cursor,
			@RequestParam(defaultValue = "10") int size,
			@RequestBody @Valid FilterPostsRequestDto requestDto) {

		PostPagingResponseDto response = postQueryFacade.getFilterPostList(requestDto, sortBy, cursor, size, userId);
		return ResponseEntity.ok(ApiResponse.success(response));
	}
}