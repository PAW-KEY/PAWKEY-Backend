package org.sopt.pawkey.backendapi.domain.post.api.controller;

import static org.sopt.pawkey.backendapi.global.constants.AppConstants.*;

import java.util.List;

import org.sopt.pawkey.backendapi.domain.category.api.dto.response.CategoryListResponseDto;
import org.sopt.pawkey.backendapi.domain.category.application.dto.result.CategoryResult;
import org.sopt.pawkey.backendapi.domain.post.api.dto.request.PostCreateRequestDto;
import org.sopt.pawkey.backendapi.domain.post.api.dto.response.PostRegisterResponseDto;
import org.sopt.pawkey.backendapi.domain.post.api.dto.response.PostResponseDto;
import org.sopt.pawkey.backendapi.domain.post.application.dto.command.PostRegisterCommand;
import org.sopt.pawkey.backendapi.domain.post.application.facade.command.PostFacade;
import org.sopt.pawkey.backendapi.domain.post.application.facade.command.PostQueryFacade;
import org.sopt.pawkey.backendapi.domain.post.application.facade.command.PostRegisterFacade;
import org.sopt.pawkey.backendapi.global.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(API_PREFIX + "/posts")
public class PostController {

	private final PostFacade postFacade;
	private final PostRegisterFacade postRegisterFacade;
	private final PostQueryFacade postQueryFacade;

	@Operation(summary = "산책 게시물 등록", description = "산책 완료 후, 산책 게시물 등록합니다. ", tags = {"Posts"})
	@ApiResponses({
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "게시물 조회 성공"),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "게시물 ID가 요청에 포함되어 있지 않습니다.", content = @Content(mediaType = "application/json")),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "해당 게시물을 찾을 수 없습니다.", content = @Content(mediaType = "application/json")),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @Content(mediaType = "application/json"))})
	@PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
	public ResponseEntity<ApiResponse<PostRegisterResponseDto>> createPost(
		@RequestHeader(USER_ID_HEADER) @NotNull Integer userId,
		@RequestPart("data") @Valid @NotNull PostCreateRequestDto requestDto,

		@RequestPart("images") @Valid @NotNull List<MultipartFile> images

	) {
		PostRegisterCommand command = requestDto.toCommand();
		PostRegisterResponseDto response = postRegisterFacade.execute(userId.longValue(), command, images);
		return ResponseEntity.ok(ApiResponse.success(response));
	}


	@Operation(summary = "게시물 상세 조회", description = "산책 게시물의 상세정보를 조회합니다.", tags = {"Posts"})
	@ApiResponses({
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "카테고리 조회 성공"),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(mediaType = "application/json")),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @Content(mediaType = "application/json"))})
	@GetMapping("/{postId}")
	public ResponseEntity<ApiResponse<PostResponseDto>> getPosts(
		@RequestHeader(USER_ID_HEADER) @NotNull Integer userId,
		@PathVariable("postId") Long postId
	) {
		PostResponseDto response = postQueryFacade.getPostDetail(postId,userId.longValue());
		return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(response));
	}


}
