package org.sopt.pawkey.backendapi.domain.post.api.dto.response;

import org.sopt.pawkey.backendapi.domain.post.infra.persistence.entity.PostEntity;

public record PostRegisterResponseDto(
	Long postId,
	Long routeId

) {
	public static PostRegisterResponseDto from(PostEntity post) {
		return new PostRegisterResponseDto(post.getPostId(),post.getRoute().getRouteId());
	}
}