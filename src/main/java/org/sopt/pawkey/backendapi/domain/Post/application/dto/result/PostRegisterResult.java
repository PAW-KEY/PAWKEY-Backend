package org.sopt.pawkey.backendapi.domain.post.application.dto.result;

import lombok.Builder;

@Builder
public record PostRegisterResult(
	Long postId,
	Long routeId
) {
}