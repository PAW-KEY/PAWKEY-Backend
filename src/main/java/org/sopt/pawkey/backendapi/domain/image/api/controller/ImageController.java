package org.sopt.pawkey.backendapi.domain.image.api.controller;

import static org.sopt.pawkey.backendapi.global.constants.AppConstants.*;

import org.sopt.pawkey.backendapi.domain.image.api.dto.request.ImageRegisterRequestDto;
import org.sopt.pawkey.backendapi.domain.image.api.dto.response.ImageRegisterResponseDto;

import org.sopt.pawkey.backendapi.domain.image.application.dto.command.RegisterImageCommand;
import org.sopt.pawkey.backendapi.domain.image.application.dto.result.RegisterImageResult;
import org.sopt.pawkey.backendapi.domain.image.application.facade.command.RegisterImageFacade;
import org.sopt.pawkey.backendapi.global.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(API_PREFIX + "/images")
@RequiredArgsConstructor
public class ImageController {

	private final RegisterImageFacade registerImageFacade;

	@PostMapping("/register")
	public ResponseEntity<ApiResponse<ImageRegisterResponseDto>> registerImage(
		@RequestBody ImageRegisterRequestDto request
	) {
		RegisterImageResult result = registerImageFacade.execute(
			new RegisterImageCommand(
				request.imageUrl(),
				request.contentType(),
				request.width(),
				request.height()
			)
		);

		return ResponseEntity.ok(
			ApiResponse.success(new ImageRegisterResponseDto(result.imageId()))
		);
	}
}