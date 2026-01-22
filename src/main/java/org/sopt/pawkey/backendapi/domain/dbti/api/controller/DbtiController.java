package org.sopt.pawkey.backendapi.domain.dbti.api.controller;

import static org.sopt.pawkey.backendapi.global.constants.AppConstants.*;

import org.sopt.pawkey.backendapi.domain.auth.annotation.UserId;
import org.sopt.pawkey.backendapi.domain.dbti.api.dto.request.DbtiSubmitRequestDto;
import org.sopt.pawkey.backendapi.domain.dbti.api.dto.response.DbtiQuestionListResponseDto;
import org.sopt.pawkey.backendapi.domain.dbti.api.dto.response.DbtiResultResponseDto;
import org.sopt.pawkey.backendapi.domain.dbti.application.facade.DbtiCommandFacade;
import org.sopt.pawkey.backendapi.domain.dbti.application.facade.query.DbtiQueryFacade;
import org.sopt.pawkey.backendapi.global.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "DBTI")
@RestController
@RequiredArgsConstructor
@RequestMapping(API_PREFIX + "/pets/dbti")
public class DbtiController {

	private final DbtiQueryFacade dbtiQueryFacade;

	@Operation(summary = "DBTI 검사 질문지 조회", description = "검사에 필요한 질문과 선택지 리스트를 조회합니다.", tags = {"DBTI"})
	@ApiResponses({
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "질문지 조회 성공"),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @Content(mediaType = "application/json"))})
	@GetMapping("/questions")
	public ResponseEntity<ApiResponse<DbtiQuestionListResponseDto>> getDbtiQuestions(
		@Parameter(hidden = true) @UserId Long userId
	) {
		DbtiQuestionListResponseDto response = dbtiQueryFacade.getDbtiQuestions();

		return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(response));
	}

	private final DbtiCommandFacade dbtiCommandFacade;

	@Operation(summary = "DBTI 검사하기", description = "유저의 답변을 기반으로 반려견의 dbti를 저장합니다.")
	@ApiResponses({
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "검사 성공"),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청 파라미터"),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "존재하지 않는 반려견 정보"),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @Content)
	})
	@PostMapping("/{petId}")
	public ResponseEntity<ApiResponse<DbtiResultResponseDto>> submitDbti(
		@Parameter(hidden = true) @UserId Long userId,
		@PathVariable Long petId,
		@RequestBody @Valid DbtiSubmitRequestDto request
	) {
		DbtiResultResponseDto response = dbtiCommandFacade.submitDbtiTest(petId, request);
		return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(response));
	}

	@Operation(summary = "DBTI 결과 조회", description = "반려견의 DBTI 검사 결과를 조회합니다.")
	@ApiResponses({
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공"),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "반려견 정보 또는 검사 결과를 찾을 수 없음"),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @Content)
	})
	@GetMapping("/{petId}")
	public ResponseEntity<ApiResponse<DbtiResultResponseDto>> getPetDbtiResult(
		@Parameter(hidden = true) @UserId Long userId,
		@PathVariable Long petId
	) {
		DbtiResultResponseDto response = dbtiQueryFacade.getPetDbtiResult(userId, petId);
		return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(response));
	}
}