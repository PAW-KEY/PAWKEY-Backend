package org.sopt.pawkey.backendapi.domain.tempImage.api.controller;

import static org.sopt.pawkey.backendapi.global.constants.AppConstants.*;

import org.sopt.pawkey.backendapi.domain.tempImage.api.dto.request.IssuePresignedUrlRequestDTO;
import org.sopt.pawkey.backendapi.domain.tempImage.api.dto.response.IssuePresignedUrlResponseDTO;
import org.sopt.pawkey.backendapi.domain.tempImage.application.dto.command.IssuePresignedUrlCommand;
import org.sopt.pawkey.backendapi.domain.tempImage.application.dto.result.IssuePresignedUrlResult;
import org.sopt.pawkey.backendapi.domain.tempImage.application.facade.ImageUploadFacade;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.sopt.pawkey.backendapi.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(API_PREFIX + "/images")
@RequiredArgsConstructor
public class ImageController {

	private final ImageUploadFacade imageUploadFacade;

	@PostMapping("/presigned")
	@Operation(summary = "이미지 업로드용 Presigned URL 발급", description = "클라이언트가 서버를 거치지 않고 S3에 직접 이미지를 업로드할 수 있도록 Presigned URL을 발급합니다.", tags = {"Image"})
	@ApiResponses({
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Presigned URL 발급 성공"),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청 (도메인 또는 contentType 오류)")
	})
	public ResponseEntity<ApiResponse<IssuePresignedUrlResponseDTO>> issuePresignedUrl(
		@RequestBody IssuePresignedUrlRequestDTO request
	) {
		IssuePresignedUrlResult result = imageUploadFacade.issuePresignedUrl(
			new IssuePresignedUrlCommand(
				request.domain(),
				request.contentType()
			)
		);

		return ResponseEntity.ok(ApiResponse.success(IssuePresignedUrlResponseDTO.from(result)));
	}
}