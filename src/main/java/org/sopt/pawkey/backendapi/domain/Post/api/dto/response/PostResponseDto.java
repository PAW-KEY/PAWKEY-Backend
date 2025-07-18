package org.sopt.pawkey.backendapi.domain.post.api.dto.response;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import org.sopt.pawkey.backendapi.domain.post.application.dto.result.GetPostResult;
import org.sopt.pawkey.backendapi.domain.user.api.dto.AuthorDto;

public record PostResponseDto(
	Long postId,
	Long routeId,
	String title,
	String content,
	boolean isLike,
	AuthorDto authorInfo,
	CategoryTagsDto categoryTags,
	String regionName,
	String createdAt,
	String routeMapImageUrl,
	List<String> walkingImageUrls
) {

	public static PostResponseDto of(
		Long postId,
		Long routeId,
		String title,
		String content,
		boolean isLike,
		AuthorDto authorInfo,
		CategoryTagsDto categoryTags,
		String regionName,
		String createdAt,
		String routeMapImageUrl,
		List<String> walkingImageUrls
	) {
		return new PostResponseDto(
			postId, routeId, title, content, isLike,
			authorInfo, categoryTags, regionName, createdAt, routeMapImageUrl, walkingImageUrls
		);
	}

	public static PostResponseDto from(GetPostResult postResult) {
		return new PostResponseDto(
			postResult.postId(),
			postResult.routeId(),
			postResult.title(),
			postResult.content(),
			postResult.isLike(),
			postResult.author(),
			new CategoryTagsDto(postResult.categoryTags()),
			postResult.regionName(),
			postResult.createdAt().format(DateTimeFormatter.ofPattern("yyyy.MM.dd(E) | a hh:mm")
				.withLocale(Locale.KOREAN)),
			postResult.routeMapImageUrl(),
			postResult.walkingImageUrls()
		);
	}

	public record CategoryTagsDto(
		List<String> categoryOptionSummary
	) {
	}

}
