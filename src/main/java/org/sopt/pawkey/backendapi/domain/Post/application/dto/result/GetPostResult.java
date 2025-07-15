package org.sopt.pawkey.backendapi.domain.post.application.dto.result;

import java.time.LocalDateTime;
import java.util.List;

import org.sopt.pawkey.backendapi.domain.user.api.dto.AuthorDto;

import lombok.Builder;

@Builder
public record GetPostResult(
	Long postId,
	Long routeId,
	String title,
	String content,
	boolean isLike,
	AuthorDto author,
	List<String> categoryTags,
	String regionName,
	LocalDateTime createdAt,
	String routeMapImageUrl,
	List<String> walkingImageUrls
) {
}
