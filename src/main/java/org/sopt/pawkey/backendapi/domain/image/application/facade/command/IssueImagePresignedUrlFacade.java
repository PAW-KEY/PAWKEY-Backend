package org.sopt.pawkey.backendapi.domain.image.application.facade.command;

import org.sopt.pawkey.backendapi.domain.tempImage.application.dto.command.IssuePresignedUrlCommand;
import org.sopt.pawkey.backendapi.domain.tempImage.application.dto.result.IssuePresignedUrlResult;
import org.sopt.pawkey.backendapi.domain.tempImage.application.service.PresignedImageService;
import org.sopt.pawkey.backendapi.domain.tempImage.domain.ImageDomain;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class IssueImagePresignedUrlFacade {

	private final PresignedImageService presignedImageService;

	public IssuePresignedUrlResult execute(IssuePresignedUrlCommand command) {
		ImageDomain imageDomain = ImageDomain.valueOf(
			command.domain().toUpperCase()
		);

		return presignedImageService.createPresignedUrl(
			imageDomain,
			command.contentType()
		);
	}

}
