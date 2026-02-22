package org.sopt.pawkey.backendapi.domain.post.application.dto.command;

import java.util.List;

import lombok.Builder;

@Builder
public record PostRegisterCommand(
		String title,
		String description,
		boolean isPublic,
		List<SelectedOptionsForCategory> selectedOptionsForCategories,
		Long routeId,
		Long routeImageId,          // 루트 이미지 (필수 1장)
		List<Long> walkImageIds    // 산책 이미지 (선택, 최대 4장)
) {}