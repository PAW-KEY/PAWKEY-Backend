package org.sopt.pawkey.backendapi.domain.review.api.dto.request;

import java.util.List;

import org.sopt.pawkey.backendapi.domain.review.application.dto.SelectedReviewSet;
import org.sopt.pawkey.backendapi.domain.review.application.dto.command.ReviewRegisterCommand;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor

public class ReviewCreateRequestDto {
	@NotNull(message = "루트 ID는 필수입니다.")
	private final Long routeId;

	@NotNull(message = "리뷰 카테고리 선택은 비어 있을 수 없습니다.")
	private final List<@Valid SelectedReviewSet> selectedReviewSetList;

	public ReviewRegisterCommand toCommand() {
		return ReviewRegisterCommand.builder()
			.routeId(this.routeId)
			.selectedReviewSetList(this.selectedReviewSetList)
			.build();
	}

}
