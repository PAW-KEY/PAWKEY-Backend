package org.sopt.pawkey.backendapi.domain.review.api.dto.response;

import java.util.List;

public record ReviewResponseDto(
	String code,
	String message,
	Data data
) {

	public static ReviewResponseDto createMock() {
		return new ReviewResponseDto(
			"S000",
			"요청 처리 성공",
			new Data(
				123L,
				42,
				List.of(
					new CategoryTop(
						1L,
						"안전",
						2L,
						"차량이 거의 다니지 않아요",
						1,
						42
					),
					new CategoryTop(
						1L,
						"안전",
						3L,
						"킥보드가 거의 없어요",
						2,
						37
					),
					new CategoryTop(
						2L,
						"편리성",
						7L,
						"조명이 밝아요",
						3,
						35
					)
				)
			)
		);
	}

	public record Data(
		Long postId,
		int totalReviewCount,
		List<CategoryTop> categoryTop3
	) {
	}

	public record CategoryTop(
		Long categoryId,
		String categoryName,
		Long categoryOptionId,
		String optionText,
		int rank,
		int percentage
	) {
	}
}