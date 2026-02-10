package org.sopt.pawkey.backendapi.domain.post.api.dto.request;

import java.util.List;

public record FilterPostsRequestDto(
	List<CategoryFilterDto> selectedOptions
) {
	public record CategoryFilterDto(
		Long categoryId,
		Long durationId,
		Object optionsIds // long값과 배열값 모두 처리 가능
	) {
		public Long getTargetId() {
			return categoryId != null ? categoryId : durationId;
		}

		public List<Long> getOptionIdList() {
			if (optionsIds instanceof List) { // 리스트(배열) 형태일 떄
				return ((List<?>)optionsIds).stream()
					.map(o -> Long.valueOf(o.toString())).toList();
			} else if (optionsIds != null) { // 단일 값일 때
				return List.of(Long.valueOf(optionsIds.toString()));
			}
			return List.of(); // 안 들어왔을 때
		}
	}
}
