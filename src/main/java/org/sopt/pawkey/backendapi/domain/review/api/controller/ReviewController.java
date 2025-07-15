package org.sopt.pawkey.backendapi.domain.review.api.controller;

import static org.sopt.pawkey.backendapi.global.constants.AppConstants.*;

import org.sopt.pawkey.backendapi.domain.review.api.dto.request.ReviewCreateRequestDto;
import org.sopt.pawkey.backendapi.domain.review.application.dto.command.ReviewRegisterCommand;
import org.sopt.pawkey.backendapi.domain.review.application.facade.ReviewRegisterFacade;
import org.sopt.pawkey.backendapi.global.response.ApiResponse;
import org.springframework.http.ResponseEntity;
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
@RequestMapping(API_PREFIX + "/reviews")
public class ReviewController {

	private final ReviewRegisterFacade reviewRegisterFacade;


	@Operation(summary = "리뷰 카테고리 등록", description = "공유된 루트로 산책 완료 후, 리뷰 카테고리를 등록합니다.", tags = {"Reviews"})
	@ApiResponses({
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "리뷰 등록 성공"),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(mediaType = "application/json")),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @Content(mediaType = "application/json"))})
	@PostMapping("")
	public ResponseEntity<ApiResponse<Void>> createPost(
		@RequestHeader(USER_ID_HEADER) @NotNull Long userId,
		@RequestBody @Valid @NotNull ReviewCreateRequestDto requestDto
	) {
		ReviewRegisterCommand command = requestDto.toCommand();

		reviewRegisterFacade.execute(userId,command);

		return ResponseEntity.ok(ApiResponse.success(null));
	}





}
