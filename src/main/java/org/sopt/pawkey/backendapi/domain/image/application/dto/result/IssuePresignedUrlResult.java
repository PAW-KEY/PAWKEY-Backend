package org.sopt.pawkey.backendapi.domain.image.application.dto.result;

public record IssuePresignedUrlResult(String uploadUrl,
									  String imageUrl) {
}
