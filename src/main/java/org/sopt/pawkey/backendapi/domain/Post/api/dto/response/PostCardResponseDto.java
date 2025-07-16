package org.sopt.pawkey.backendapi.domain.post.api.dto.response;

import java.time.format.DateTimeFormatter;
import java.util.List;

import org.sopt.pawkey.backendapi.domain.post.application.dto.result.GetPostCardResult;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record PostCardResponseDto(
	Long postId,
	String createdAt,
	boolean isLike,
	String title,
	String representativeImageUrl,
	Long routeId,
	WriterDto writer,
	List<String> descriptionTags,
	boolean isPublic,
	boolean isMine
) {

	public static PostCardResponseDto from(GetPostCardResult result) {
		return new PostCardResponseDto(
			result.postId(),
			result.createdAt().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")),
			result.isLike(),
			result.title(),
			result.routeMapImageUrl(),
			result.routeId(),
			new WriterDto(
				result.author().authorId(),
				result.author().petName(),
				result.author().petProfileImage()
			),
			result.categoryTags(),
			result.isPublic(),
			result.isMine()
		);
	}

	public boolean getIsPublic() {
		return isPublic;
	}

	public record WriterDto(
		Long userId,
		String petName,
		String petProfileImageUrl
	) {
	}
}
