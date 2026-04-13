package org.sopt.pawkey.backendapi.domain.review.api.controller;

import static org.sopt.pawkey.backendapi.global.constants.AppConstants.*;

import io.swagger.v3.oas.annotations.Parameter;
import org.sopt.pawkey.backendapi.domain.auth.annotation.UserId;
import org.sopt.pawkey.backendapi.domain.review.api.dto.request.ReviewCreateRequestDto;
import org.sopt.pawkey.backendapi.domain.review.api.dto.response.ReviewHeaderResponseDto;
import org.sopt.pawkey.backendapi.domain.review.application.dto.command.ReviewRegisterCommand;
import org.sopt.pawkey.backendapi.domain.review.application.dto.result.ReviewHeaderResult;
import org.sopt.pawkey.backendapi.domain.review.application.facade.GetReviewHeaderFacade;
import org.sopt.pawkey.backendapi.domain.review.application.facade.ReviewRegisterFacade;
import org.sopt.pawkey.backendapi.global.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(API_PREFIX + "/reviews")
public class ReviewController {

	private final ReviewRegisterFacade reviewRegisterFacade;
	private final GetReviewHeaderFacade getReviewHeaderFacade;

	@Operation(summary = "리뷰 카테고리 등록", description = "공유된 루트로 산책 완료 후, 리뷰 카테고리를 등록합니다.", tags = {"Reviews"})
	@ApiResponses({
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "리뷰 등록 성공"),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(mediaType = "application/json")),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @Content(mediaType = "application/json"))})
	@PostMapping("")
	public ResponseEntity<ApiResponse<Void>> createPost(
		@NotNull @UserId Long userId,
		@RequestBody @Valid @NotNull ReviewCreateRequestDto requestDto
	) {
		ReviewRegisterCommand command = requestDto.toCommand();

		reviewRegisterFacade.execute(userId, command);

		return ResponseEntity.ok(ApiResponse.success(null));
	}



	@Operation(summary = "리뷰 메타데이터 조회", description = "리뷰 작성 화면 상단에 표시할 게시물 제목 및 후기 작성자 프로필을 조회합니다.", tags = {"Reviews"})
	@ApiResponses({
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공"),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "존재하지 않는 게시물", content = @Content(mediaType = "application/json")),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @Content(mediaType = "application/json"))
	})
	@GetMapping("/{postId}/review-header")
	public ResponseEntity<ApiResponse<ReviewHeaderResponseDto>> getReviewHeader(
			@Parameter(hidden = true) @UserId Long userId,
			@PathVariable Long postId
	) {
		ReviewHeaderResult result = getReviewHeaderFacade.execute(postId, userId);
		return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(ReviewHeaderResponseDto.from(result)));
	}

}
