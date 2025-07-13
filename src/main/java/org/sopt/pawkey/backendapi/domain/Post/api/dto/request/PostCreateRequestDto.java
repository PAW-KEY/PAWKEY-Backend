package org.sopt.pawkey.backendapi.domain.post.api.dto.request;

import java.util.List;

import org.sopt.pawkey.backendapi.domain.post.application.dto.command.SelectedOptionsForCategory;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PostCreateRequestDto {
	private final Long userId;
	private final String title;
	private final String content;
	private final List<SelectedOptionsForCategory> selectedOptionsForCategories;



}
