package org.sopt.pawkey.backendapi.domain.post.application.dto.command;

import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SelectedOptionsForCategory {
	private final Long categoryId;
	private final List<Long> selectedOptionIds;
}