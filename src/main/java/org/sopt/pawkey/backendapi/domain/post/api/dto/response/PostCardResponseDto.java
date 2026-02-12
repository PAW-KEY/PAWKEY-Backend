package org.sopt.pawkey.backendapi.domain.post.api.dto.response;

import java.time.LocalDateTime;

import org.sopt.pawkey.backendapi.domain.post.application.dto.result.GetPostCardResult;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record PostCardResponseDto(
	Long postId,
	String regionName,
	String title,
	LocalDateTime date,
	Integer durationMinutes,
	boolean isLiked,
	String imageUrl
) {

	public static PostCardResponseDto from(GetPostCardResult result) {
		return new PostCardResponseDto(
			result.postId(),
			result.regionName(),
			result.title(),
			result.createdAt(),
			result.durationMinutes(),
			result.isLike(),
			result.routeMapImageUrl()
		);
	}
}