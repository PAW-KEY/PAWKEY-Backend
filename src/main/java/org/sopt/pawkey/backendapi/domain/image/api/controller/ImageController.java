package org.sopt.pawkey.backendapi.domain.image.api.controller;

import static org.sopt.pawkey.backendapi.global.constants.AppConstants.*;

import org.sopt.pawkey.backendapi.domain.image.api.dto.request.ImageRegisterRequestDto;
import org.sopt.pawkey.backendapi.domain.image.api.dto.response.ImageRegisterResponseDto;

import org.sopt.pawkey.backendapi.domain.image.application.dto.command.RegisterImageCommand;
import org.sopt.pawkey.backendapi.domain.image.application.dto.result.RegisterImageResult;
import org.sopt.pawkey.backendapi.domain.image.application.facade.command.RegisterImageFacade;
import org.sopt.pawkey.backendapi.domain.image.api.dto.request.IssuePresignedUrlRequestDTO;
import org.sopt.pawkey.backendapi.domain.image.api.dto.response.IssuePresignedUrlResponseDTO;
import org.sopt.pawkey.backendapi.domain.image.application.dto.result.IssuePresignedUrlResult;
import org.sopt.pawkey.backendapi.domain.image.application.service.PresignedImageService;
import org.sopt.pawkey.backendapi.domain.image.domain.ImageDomain;
import org.sopt.pawkey.backendapi.global.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(API_PREFIX + "/images")
@RequiredArgsConstructor
public class ImageController {

	private final PresignedImageService presignedImageService;
	private final RegisterImageFacade registerImageFacade;

	@PostMapping("/presigned")
	@Operation(summary = "이미지 업로드용 Presigned URL 발급", description = "클라이언트가 서버를 거치지 않고 S3에 직접 이미지를 업로드할 수 있도록 Presigned URL을 발급합니다.", tags = {"Image"})
	@ApiResponses({
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Presigned URL 발급 성공"),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청 (도메인 또는 contentType 오류)")
	})
	public ResponseEntity<ApiResponse<IssuePresignedUrlResponseDTO>> issuePresignedUrl(
		@RequestBody IssuePresignedUrlRequestDTO request
	) {
		ImageDomain domain = request.getDomainEnum();
		IssuePresignedUrlResult result = presignedImageService.createPresignedUrl(domain,request.contentType());

		return ResponseEntity.ok(ApiResponse.success(IssuePresignedUrlResponseDTO.from(result)));
	}

	@PostMapping("/register")
	public ResponseEntity<ApiResponse<ImageRegisterResponseDto>> registerImage(
		@RequestBody ImageRegisterRequestDto request
	) {
		RegisterImageResult result = registerImageFacade.execute(
			new RegisterImageCommand(
				request.imageUrl(),
				request.contentType(),
				request.width(),
				request.height(),
			    request.getDomainEnum()
			)
		);

		return ResponseEntity.ok(
			ApiResponse.success(new ImageRegisterResponseDto(result.imageId()))
		);
	}
}