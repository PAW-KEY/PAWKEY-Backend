package org.sopt.pawkey.backendapi.domain.post.api.controller;

import static org.sopt.pawkey.backendapi.global.constants.AppConstants.*;

import java.util.List;

import org.sopt.pawkey.backendapi.domain.post.api.dto.request.PostCreateRequestDto;
import org.sopt.pawkey.backendapi.domain.post.application.dto.command.PostRegisterCommand;
import org.sopt.pawkey.backendapi.domain.post.application.facade.command.PostFacade;
import org.sopt.pawkey.backendapi.domain.post.application.facade.command.PostRegisterFacade;
import org.sopt.pawkey.backendapi.global.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

	@Operation(summary = "산책 게시물 등록", description = "산책 완료 후, 산책 게시물 등록합니다. ", tags = {"Posts"})
	@ApiResponses({
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "산책 게시물 등록 성공"),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "제목/본문 누락 또는 길이 제한 초과", content = @Content(mediaType = "application/json")),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "선택한 카테고리 또는 옵션이 유효하지 않음", content = @Content(mediaType = "application/json")),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "이미지 파일 형식 또는 용량 오류", content = @Content(mediaType = "application/json")),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @Content(mediaType = "application/json"))})
	@PostMapping("")
	public ResponseEntity<ApiResponse<Void>> createPost(
		@RequestHeader(USER_ID_HEADER) @NotNull Integer userId,
		@RequestPart("data") @Valid @NotNull PostCreateRequestDto requestDto,

		@RequestPart("images") @Valid @NotNull List<MultipartFile> images

	) {
		PostRegisterCommand command = requestDto.toCommand();
		postFacade.createPost(userId.longValue(), requestDto, images);
		return ResponseEntity.ok(ApiResponse.success(null));
	}

}
