package org.sopt.pawkey.backendapi.domain.tempImage.application.dto.result;

public record IssuePresignedUrlResult(String uploadUrl,
									  String imageUrl) {
}
