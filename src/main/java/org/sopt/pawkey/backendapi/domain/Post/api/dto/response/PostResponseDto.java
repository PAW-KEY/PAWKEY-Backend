package org.sopt.pawkey.backendapi.domain.post.api.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import org.sopt.pawkey.backendapi.domain.category.api.dto.CategorySummaryTagsDto;
import org.sopt.pawkey.backendapi.domain.user.api.dto.AuthorDto;

public record PostResponseDto(
	Long postId,
	Long routeId,
	String title,
	String content,
	boolean isLike,
	AuthorDto author,
	CategoryTagsDto categoryTags,
	String regionName,
	LocalDateTime createdAt,
	String routeMapImageUrl,
	List<String> walkingImageUrls
) {

	public static PostResponseDto of(
		Long postId,
		Long routeId,
		String title,
		String content,
		boolean isLike,
		AuthorDto author,
		CategoryTagsDto categoryTags,
		String regionName,
		LocalDateTime createdAt,
		String routeMapImageUrl,
		List<String> walkingImageUrls
	) {
		return new PostResponseDto(
			postId, routeId, title, content, isLike,
			author, categoryTags, regionName, createdAt, routeMapImageUrl, walkingImageUrls
		);
	}


	public record CategoryTagsDto(
		List<String> categoryOptionSummary
	) {}


}
