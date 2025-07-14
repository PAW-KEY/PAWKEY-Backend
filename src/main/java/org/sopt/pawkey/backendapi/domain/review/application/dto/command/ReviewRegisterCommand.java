package org.sopt.pawkey.backendapi.domain.review.application.dto.command;

import java.util.List;

import org.sopt.pawkey.backendapi.domain.review.application.dto.selectedReviewSet;

import lombok.Builder;

@Builder
public record ReviewRegisterCommand(
	Long routeId,
	List<selectedReviewSet> selectedReviewSetList) {

}
