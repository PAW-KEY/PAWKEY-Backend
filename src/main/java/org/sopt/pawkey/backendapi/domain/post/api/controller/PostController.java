package org.sopt.pawkey.backendapi.domain.post.api.controller;

import static org.sopt.pawkey.backendapi.global.constants.AppConstants.*;

import java.util.List;

import org.sopt.pawkey.backendapi.domain.auth.annotation.UserId;
import org.sopt.pawkey.backendapi.domain.post.api.dto.request.PostCreateRequestDto;
import org.sopt.pawkey.backendapi.domain.post.api.dto.request.PostUpdateRequestDto;
import org.sopt.pawkey.backendapi.domain.post.api.dto.response.PostDetailResponseDto;
import org.sopt.pawkey.backendapi.domain.post.api.dto.response.PostRegisterResponseDto;
import org.sopt.pawkey.backendapi.domain.post.api.dto.response.PostResponseDto;
import org.sopt.pawkey.backendapi.domain.post.application.dto.command.PostRegisterCommand;
import org.sopt.pawkey.backendapi.domain.post.application.dto.result.PostRegisterResult;
import org.sopt.pawkey.backendapi.domain.post.application.facade.command.PostDeleteFacade;
import org.sopt.pawkey.backendapi.domain.post.application.facade.command.PostRegisterFacade;
import org.sopt.pawkey.backendapi.domain.post.application.facade.command.PostUpdateFacade;
import org.sopt.pawkey.backendapi.domain.post.application.facade.query.PostQueryFacade;
import org.sopt.pawkey.backendapi.domain.review.api.dto.response.ReviewResponseDto;
import org.sopt.pawkey.backendapi.global.response.ApiResponse;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
@RequestMapping(API_PREFIX + "/posts")
public class PostController {

	private final PostRegisterFacade postRegisterFacade;
	private final PostUpdateFacade postUpdateFacade;
	private final PostDeleteFacade postDeleteFacade;

	private final PostQueryFacade postQueryFacade;

	@Operation(summary = "산책 게시물 등록", description = "산책 완료 후 작성한 산책 게시물을 등록합니다.", tags = {"Posts"})
	@ApiResponses({
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "게시물 조회 성공"),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "게시물 ID가 요청에 포함되어 있지 않습니다.", content = @Content(mediaType = "application/json")),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "해당 게시물을 찾을 수 없습니다.", content = @Content(mediaType = "application/json")),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @Content(mediaType = "application/json"))})

	@PostMapping
	public ResponseEntity<ApiResponse<PostRegisterResponseDto>> createPost(
		@Parameter(hidden = true) @UserId Long userId,
		@RequestBody @Valid PostCreateRequestDto requestDto
	) {
		PostRegisterResult result = postRegisterFacade.execute(userId, requestDto.toCommand());
		return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(PostRegisterResponseDto.from(result)));
	}


	@Operation(summary = "산책 게시물 수정", description = "기존 산책 게시물을 수정합니다.", tags = {"Posts"})
	@PatchMapping("/{postId}")
	public ResponseEntity<ApiResponse<PostRegisterResponseDto>> updatePost(
		@Parameter(hidden = true) @UserId Long userId,
		@PathVariable Long postId,
		@RequestBody @Valid PostUpdateRequestDto requestDto
	) {
		PostRegisterResult result = postUpdateFacade.execute(userId, postId, requestDto.toCommand());
		return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(PostRegisterResponseDto.from(result)));
	}

	@Operation(summary = "산책 게시물 삭제", description = "기존 산책 게시물을 삭제합니다.", tags = {"Posts"})
	@DeleteMapping("/{postId}")
	public ResponseEntity<ApiResponse<Void>> deletePost(
		@Parameter(hidden = true) @UserId Long userId,
		@PathVariable Long postId
	) {
		postDeleteFacade.execute(userId, postId);
		return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(null));
	}

	@Operation(summary = "게시물 상세 조회", description = "산책 게시물의 상세정보를 조회합니다.", tags = {"Posts"})
	@GetMapping("/{postId}")
	@ApiResponses({
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "게시물 상세 조회 성공"),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(mediaType = "application/json")),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @Content(mediaType = "application/json"))})

	public ResponseEntity<ApiResponse<PostDetailResponseDto>> getPosts(
		@Parameter(hidden = true) @UserId Long userId,
		@PathVariable("postId") Long postId
	) {
		PostDetailResponseDto response = postQueryFacade.getPostDetail(postId, userId);
		return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(response));
	}



	@Operation(summary = "리뷰 TOP3 조회  api", description = "리뷰 TOP3 를 반환합니다. 조회  api.", tags = {"Posts"})
	@ApiResponses({
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "리뷰 조회 성공"),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(mediaType = "application/json")),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @Content(mediaType = "application/json"))})
	@GetMapping("/{routeId}/reviews/top")
	public ResponseEntity<ApiResponse<ReviewResponseDto>> getTopReviewsForPost(
		@UserId Long userId,
		@PathVariable("routeId") Long routeId
	) {
		ReviewResponseDto response = ReviewResponseDto.createMock();
		return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(response));
	}

}
