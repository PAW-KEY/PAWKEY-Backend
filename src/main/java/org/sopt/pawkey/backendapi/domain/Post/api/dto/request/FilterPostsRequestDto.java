package org.sopt.pawkey.backendapi.domain.post.api.dto.request;

import java.util.List;

public record FilterPostsRequestDto(
	int durationStart,
	int durationEnd,
	List<CategoryFilterDto> selectedOptions) {

	public record CategoryFilterDto(
		Long categoryId,
		List<Long> optionsIds) {
	}
}
