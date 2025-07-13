package org.sopt.pawkey.backendapi.domain.user.api.dto.response;

import java.util.List;

public record LikedPostsResponseWrapperDto(
	List<LikedPostResponseDto> posts
) {
}