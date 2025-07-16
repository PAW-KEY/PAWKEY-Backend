package org.sopt.pawkey.backendapi.domain.post.api.dto.response;

import org.sopt.pawkey.backendapi.domain.post.application.dto.result.PostRegisterResult;

public record PostRegisterResponseDto(
	Long postId,
	Long routeId
) {
	public static PostRegisterResponseDto from(PostRegisterResult postRegisterResult) {
		return new PostRegisterResponseDto(postRegisterResult.postId(), postRegisterResult.routeId());
	}
}