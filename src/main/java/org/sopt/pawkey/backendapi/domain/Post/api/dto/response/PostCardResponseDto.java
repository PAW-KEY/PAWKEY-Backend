package org.sopt.pawkey.backendapi.domain.post.api.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import org.sopt.pawkey.backendapi.domain.post.application.dto.result.GetPostCardResult;

public record PostCardResponseDto(
	Long postId,
	LocalDateTime createdAt,
	Boolean isLike,
	String title,
	String representativeImageUrl,
	WriterDto writer,
	List<String> descriptionTags
) {

	public static PostCardResponseDto of(
		Long postId,
		LocalDateTime createdAt,
		Boolean isLike,
		String title,
		String representativeImageUrl,
		WriterDto writer,
		List<String> descriptionTags
	) {
		return new PostCardResponseDto(
			postId,
			createdAt,
			isLike,
			title,
			representativeImageUrl,
			writer,
			descriptionTags
		);
	}

	public static PostCardResponseDto from(GetPostCardResult result) {
		return new PostCardResponseDto(
			result.postId(),
			result.createdAt(),
			result.isLike(),
			result.title(),
			result.routeMapImageUrl(),
			new WriterDto(
				result.author().authorId(),
				result.author().petName(),
				result.author().petProfileImage()
			),
			result.categoryTags()
		);
	}

	public record WriterDto(
		Long userId,
		String petName,
		String petProfileImageUrl
	) {
	}
}