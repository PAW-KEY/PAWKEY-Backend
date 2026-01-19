package org.sopt.pawkey.backendapi.domain.tempImage.application.facade;

import org.sopt.pawkey.backendapi.domain.tempImage.api.dto.response.IssuePresignedUrlResponseDTO;
import org.sopt.pawkey.backendapi.domain.tempImage.application.dto.command.IssuePresignedUrlCommand;
import org.sopt.pawkey.backendapi.domain.tempImage.application.dto.result.IssuePresignedUrlResult;
import org.sopt.pawkey.backendapi.domain.tempImage.application.service.PresignedImageService;
import org.sopt.pawkey.backendapi.domain.tempImage.domain.ImageDomain;
import org.springframework.stereotype.Component;


import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ImageUploadFacade {

	private final PresignedImageService presignedImageService;

	public IssuePresignedUrlResult issuePresignedUrl(IssuePresignedUrlCommand command) {
		ImageDomain imageDomain = ImageDomain.valueOf(
			command.domain().toUpperCase()
		);

		return presignedImageService.createPresignedUrl(
			imageDomain,
			command.contentType()
		);
	}

}
