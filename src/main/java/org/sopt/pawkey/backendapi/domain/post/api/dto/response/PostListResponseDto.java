package org.sopt.pawkey.backendapi.domain.post.api.dto.response;

import java.util.List;

public record PostListResponseDto(
	List<PostCardResponseDto> posts
) {
}