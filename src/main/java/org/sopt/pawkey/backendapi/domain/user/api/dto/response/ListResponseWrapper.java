package org.sopt.pawkey.backendapi.domain.user.api.dto.response;

import java.util.List;

/**
 * 리스트 형태 응답을 감싸기 위한 PostList Wrapper DTO
 * 예: { "posts": [...] }
 */
public record ListResponseWrapper<T>(
	List<T> posts
) {
	public static <T> ListResponseWrapper<T> from(List<T> list) {
		return new ListResponseWrapper<>(list);
	}
}