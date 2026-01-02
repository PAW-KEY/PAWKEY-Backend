package org.sopt.pawkey.backendapi.domain.post.application.dto.result;

import java.time.LocalDateTime;
import java.util.List;

import org.sopt.pawkey.backendapi.domain.user.api.dto.AuthorDto;

import lombok.Builder;

@Builder
public record GetPostCardResult(
	Long postId,
	String regionName,
	String title,
	LocalDateTime createdAt,
	Integer durationMinutes,
	boolean isLike,
	String routeMapImageUrl,
	boolean isPublic
) {
}
