package org.sopt.pawkey.backendapi.domain.post.api.dto.response;

import java.util.List;

import org.sopt.pawkey.backendapi.domain.user.api.dto.response.MyPostResponseDto;

public record PostListResponseDto(
	List<PostCardResponseDto> posts
) {
}