package org.sopt.pawkey.backendapi.domain.post.api.dto.request;

import java.util.List;

import org.sopt.pawkey.backendapi.domain.post.application.dto.command.PostRegisterCommand;
import org.sopt.pawkey.backendapi.domain.post.application.dto.command.SelectedOptionsForCategory;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PostCreateRequestDto {
	private final Long userId;
	private final String title;
	private final String description;
	private final boolean isPublic;
	private final List<SelectedOptionsForCategory> selectedOptionsForCategories;
	private final Long routeId;

	public PostRegisterCommand toCommand() {
	}
}
