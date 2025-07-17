package org.sopt.pawkey.backendapi.domain.user.api.controller;

import static org.sopt.pawkey.backendapi.global.constants.AppConstants.*;

import org.sopt.pawkey.backendapi.domain.image.application.service.command.ImageService;
import org.sopt.pawkey.backendapi.domain.image.infra.persistence.entity.ImageEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(API_PREFIX + "/s3")
@RequiredArgsConstructor
public class S3Controller {

	private final ImageService imageService;

	@PostMapping(value = "/upload/profiles", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
	public ResponseEntity<ImageEntity> uploadProfileImage(@RequestParam("image") MultipartFile image) {

		return ResponseEntity.ok(imageService.storePetProfileImage(image));
	}
}