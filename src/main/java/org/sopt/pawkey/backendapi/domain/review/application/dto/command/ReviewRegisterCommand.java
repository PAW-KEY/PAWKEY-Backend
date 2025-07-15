package org.sopt.pawkey.backendapi.domain.review.application.dto.command;

import java.util.List;

import org.sopt.pawkey.backendapi.domain.review.application.dto.SelectedReviewSet;

import lombok.Builder;

@Builder
public record ReviewRegisterCommand(
	Long routeId,
	List<SelectedReviewSet> selectedReviewSetList) {

}
