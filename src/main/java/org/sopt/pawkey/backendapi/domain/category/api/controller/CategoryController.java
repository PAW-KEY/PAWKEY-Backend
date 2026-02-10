package org.sopt.pawkey.backendapi.domain.category.api.controller;

import static org.sopt.pawkey.backendapi.global.constants.AppConstants.*;

import java.util.List;

import org.sopt.pawkey.backendapi.domain.auth.annotation.UserId;
import org.sopt.pawkey.backendapi.domain.category.api.dto.response.CategoryListResponseDto;
import org.sopt.pawkey.backendapi.domain.category.api.dto.response.PostCategoryListResponseDto;
import org.sopt.pawkey.backendapi.domain.category.application.dto.result.CategoryResult;
import org.sopt.pawkey.backendapi.domain.category.application.facade.query.CategoryQueryFacade;
import org.sopt.pawkey.backendapi.domain.category.application.service.CategoryQueryService;
import org.sopt.pawkey.backendapi.global.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(API_PREFIX + "/posts/categories")
public class CategoryController {
	private final CategoryQueryService categoryQueryService;

	@Operation(summary = "게시물 등록시, 카테고리 리스트 조회", description = "게사물 등록 과정에서 필요한 카테고리 정보를 리스트 형식으로 조회합니다", tags = {
		"Posts"})
	@ApiResponses({
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "카테고리 조회 성공"),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(mediaType = "application/json")),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @Content(mediaType = "application/json"))})
	@GetMapping("")
	public ResponseEntity<ApiResponse<CategoryListResponseDto>> getCategories(
	) {
		List<CategoryResult> resultList = categoryQueryService.getAllCategories();
		CategoryListResponseDto response = CategoryListResponseDto.from(resultList);
		return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(response));
	}

	private final CategoryQueryFacade categoryQueryFacade;

	@Operation(summary = "필터링 카테고리 리스트 조회", description = "게사물 필터링에서 필요한 카테고리 정보를 리스트 형식으로 조회합니다", tags = {"Posts"})
	@ApiResponses({
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "카테고리 조회 성공"),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(mediaType = "application/json")),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @Content(mediaType = "application/json"))})
	@GetMapping("/filter")
	public ResponseEntity<ApiResponse<PostCategoryListResponseDto>> getCategories(
		@Parameter(hidden = true) @UserId Long userId) {
		PostCategoryListResponseDto response = categoryQueryFacade.getFilterOptions(userId);
		return ResponseEntity.ok(ApiResponse.success(response));
	}
}


