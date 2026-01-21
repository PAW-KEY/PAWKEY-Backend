package org.sopt.pawkey.backendapi.domain.image.application.facade.command;

import org.sopt.pawkey.backendapi.domain.image.application.dto.command.RegisterImageCommand;
import org.sopt.pawkey.backendapi.domain.image.application.dto.result.RegisterImageResult;
import org.sopt.pawkey.backendapi.domain.image.application.service.command.ImageService;
import org.sopt.pawkey.backendapi.domain.image.infra.persistence.entity.ImageEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@Transactional
public class RegisterImageFacade { //이미지 메타데이터 등록

	private final ImageService imageService;

	public RegisterImageResult execute(RegisterImageCommand command) {

		ImageEntity image = imageService.createUploadedImage(
			command.imageUrl(),
			command.contentType(),
			command.width(),
			command.height()
		);

		return RegisterImageResult.from(image);
	}
}