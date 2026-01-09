package org.sopt.pawkey.backendapi.domain.dbti.api.controller;

import static org.sopt.pawkey.backendapi.global.constants.AppConstants.*;

import org.sopt.pawkey.backendapi.domain.dbti.api.dto.response.DbtiQuestionListResponseDto;
import org.sopt.pawkey.backendapi.domain.dbti.application.facade.query.DbtiQueryFacade;
import org.sopt.pawkey.backendapi.global.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
	public ResponseEntity<ApiResponse<DbtiQuestionListResponseDto>> getDbtiQuestions() {
		DbtiQuestionListResponseDto response = dbtiQueryFacade.getDbtiQuestions();

		return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(response));
	}
}