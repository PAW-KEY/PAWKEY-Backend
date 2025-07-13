package org.sopt.pawkey.backendapi.domain.post.application.dto.command;

import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PostCreateCommand {
	private final String title;
	private final String description;
	private final boolean isPublic;
	private final List<SelectedOptionsForCategory> selectedOptionsForCategories;
	private final String routeImageUrl;
	private final List<String> postImageUrlList;
	private final Long routeId;




}
