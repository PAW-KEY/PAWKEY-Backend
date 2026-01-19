package org.sopt.pawkey.backendapi.domain.tempImage.api.dto.response;

import org.sopt.pawkey.backendapi.domain.tempImage.application.dto.result.IssuePresignedUrlResult;

public record IssuePresignedUrlResponseDTO(
	String uploadUrl, // PUT용 Presigned URL
	String imageUrl // DB에 저장할 URL
) {
	public static IssuePresignedUrlResponseDTO from(IssuePresignedUrlResult result) {
	return new IssuePresignedUrlResponseDTO(
			result.uploadUrl(),
			result.imageUrl()
		);
	}
}
