package org.sopt.pawkey.backendapi.domain.post.application.dto.command;

import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PostCreateCommand {
	private final Long userId;
	private final String title;
	private final String content;
	private final List<SelectedOptionsForCategory> selectedOptionsForCategories;
	private final String routeImageUrl;
	private final List<String> postImageUrlList;




}
