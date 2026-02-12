package org.sopt.pawkey.backendapi.domain.post.api.dto.request;

import java.util.List;

import org.sopt.pawkey.backendapi.domain.post.exception.PostBusinessException;
import org.sopt.pawkey.backendapi.domain.post.exception.PostErrorCode;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.v3.oas.annotations.media.Schema;

public record FilterPostsRequestDto(
	List<CategoryFilterDto> selectedOptions
) {
	public record CategoryFilterDto(
		Long categoryId,
		Long durationId,

		@Schema(
			description = "옵션 ID 세트 (단일 숫자 22 혹은 배열 [7, 9] 모두 가능)",
			type = "integer",
			example = "0"
		)
		Object optionsIds // long값과 배열값 모두 처리 가능
	) {
		@JsonIgnore
		public Long getTargetId() {
			return categoryId != null ? categoryId : durationId;
		}

		@JsonIgnore
		public List<Long> getOptionIdList() {
			try {
				if (optionsIds instanceof List) { // 리스트(배열) 형태일 때
					return ((List<?>)optionsIds).stream()
						.filter(java.util.Objects::nonNull)
						.map(o -> Long.valueOf(o.toString()))
						.toList();
				} else if (optionsIds != null) { // 단일 값일 때
					return List.of(Long.valueOf(optionsIds.toString()));
				}
			} catch (NumberFormatException e) {
				throw new PostBusinessException(PostErrorCode.INVALID_FILTER_OPTION);
			}
			return List.of(); // 값이 들어오지 않았을 때
		}
	}
}