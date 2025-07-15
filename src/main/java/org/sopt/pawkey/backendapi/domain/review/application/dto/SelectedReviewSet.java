package org.sopt.pawkey.backendapi.domain.review.application.dto;

import java.util.List;

public record SelectedReviewSet(
	Long reviewCategoryId,
	List<Long> selectedReviewOptionIds
) {
}
