package org.sopt.pawkey.backendapi.domain.post.application.dto.command;

import java.util.List;

import lombok.Builder;

@Builder
public record PostRegisterCommand(
	String title,
	String description,
	boolean isPublic,
	List<SelectedOptionsForCategory> selectedOptionsForCategories,
	Long routeId
) {
}