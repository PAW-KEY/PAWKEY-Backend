package org.sopt.pawkey.backendapi.domain.post.api.controller;

import static org.sopt.pawkey.backendapi.global.constants.AppConstants.*;

import org.sopt.pawkey.backendapi.domain.post.application.facade.command.PostLikeFacade;
import org.sopt.pawkey.backendapi.global.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(API_PREFIX + "/likes")
public class PostLikeController {

	private final PostLikeFacade postLikeFacade;

	@Operation(summary = "좋아요 저장", description = "산책 루트 게시글에 좋아요를 저장합니다.", tags = {"Posts"})
	@ApiResponses({
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "좋아요 저장 성공"),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(mediaType = "application/json")),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "본인 게시글에 좋아요", content = @Content(mediaType = "application/json")),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "이미 요청된 좋아요", content = @Content(mediaType = "application/json")),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @Content(mediaType = "application/json"))})
	@PostMapping("/{postId}")
	public ResponseEntity<ApiResponse<Void>> like(
		@PathVariable Long postId,
		@RequestHeader(USER_ID_HEADER) @NotNull Integer userId
	) {
		postLikeFacade.like(postId, userId.longValue());
		return ResponseEntity.ok(ApiResponse.success(null));
	}

	@Operation(summary = "좋아요 취소", description = "산책 루트 게시글에 설정된 좋아요를 취소합니다.", tags = {"Posts"})
	@ApiResponses({
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "좋아요 취소 성공"),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(mediaType = "application/json")),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "본인 게시글에 좋아요 취소", content = @Content(mediaType = "application/json")),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "존재하지 않는 게시글", content = @Content(mediaType = "application/json")),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "좋아요 기록 없음", content = @Content(mediaType = "application/json")),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @Content(mediaType = "application/json"))})
	@DeleteMapping("/{postId}")
	public ResponseEntity<ApiResponse<Void>> cancelLike(
		@PathVariable Long postId,
		@RequestHeader(USER_ID_HEADER) @NotNull Integer userId) {

		postLikeFacade.cancelLike(postId, userId.longValue());
		return ResponseEntity.ok(ApiResponse.success(null));
	}
}