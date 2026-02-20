package org.sopt.pawkey.backendapi.domain.category.application.facade.query;

import java.util.List;

import org.sopt.pawkey.backendapi.domain.category.api.dto.response.PostCategoryListResponseDto;
import org.sopt.pawkey.backendapi.domain.category.application.dto.result.CategoryResult;
import org.sopt.pawkey.backendapi.domain.category.application.service.CategoryQueryService;
import org.sopt.pawkey.backendapi.domain.category.application.service.DurationQueryService;
import org.sopt.pawkey.backendapi.domain.user.application.service.UserService;
import org.springframework.stereotype.Component;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@Transactional
public class CategoryQueryFacade {
	private final CategoryQueryService categoryQueryService;
	private final DurationQueryService durationQueryService;
	private final UserService userService;

	public PostCategoryListResponseDto getFilterOptions(Long userId) {
		userService.findById(userId);
		List<CategoryResult> durations = durationQueryService.getAllDurations();
		List<CategoryResult> categories = categoryQueryService.getAllCategories();
		return PostCategoryListResponseDto.of(durations, categories);
	}
}
