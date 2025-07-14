package org.sopt.pawkey.backendapi.domain.review.application.dto;

import java.util.List;

import lombok.Getter;


public record selectedReviewSet(
	Long reviewCategoryId,
	List<Long> selectedReviewOptionIds
) {
}
