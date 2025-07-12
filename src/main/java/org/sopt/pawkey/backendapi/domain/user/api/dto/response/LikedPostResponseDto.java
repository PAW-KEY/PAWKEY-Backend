package org.sopt.pawkey.backendapi.domain.user.api.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record LikedPostResponseDto(
	Long postId,
	LocalDateTime createdAt,
	Boolean isLiked,
	String representativeImageUrl,
	WriterDto writer,
	List<String> descriptionTags
) {

	public record WriterDto(
		Long userId,
		String petName,
		String petProfileImageUrl
	) {}
}