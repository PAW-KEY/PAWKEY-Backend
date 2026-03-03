package org.sopt.pawkey.backendapi.domain.user.api.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import org.sopt.pawkey.backendapi.domain.post.infra.persistence.entity.PostEntity;
import org.sopt.pawkey.backendapi.domain.review.infra.persistence.entity.ReviewEntity;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ReviewCardResponseDto(
	Long postId,
	String title,
	String regionName,
	LocalDateTime date,
	List<String> categoryOptionSummary
) {
	public static ReviewCardResponseDto fromReview(ReviewEntity review, PostEntity post) {
		var route = review.getRoute();

		return new ReviewCardResponseDto(
			post.getPostId(),
			post.getTitle(),
			route.getRegion().getFullRegionName(),
			post.getCreatedAt(),
			post.getPostSelectedCategoryOptionEntityList().stream()
				.map(sel -> sel.getCategoryOption().getOptionValue())
				.toList()
		);
	}
}