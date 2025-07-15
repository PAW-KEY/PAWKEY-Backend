package org.sopt.pawkey.backendapi.domain.user.api.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import org.sopt.pawkey.backendapi.domain.post.infra.persistence.entity.PostEntity;

public record MyPostResponseDto(
	Long postId,
	LocalDateTime createdAt,
	Boolean isPublic,
	String title,
	String representativeImageUrl,
	WriterDto writer,
	List<String> descriptionTags
) {

	public static MyPostResponseDto of(
		PostEntity post,
		String representativeImageUrl,
		WriterDto writer,
		List<String> descriptionTags
	) {
		return new MyPostResponseDto(
			post.getPostId(),
			post.getCreatedAt(),
			post.isPublic(),
			post.getTitle(),
			representativeImageUrl,
			writer,
			descriptionTags
		);
	}

	public record WriterDto(
		Long userId,
		String petName,
		String petProfileImageUrl
	) {
	}
}