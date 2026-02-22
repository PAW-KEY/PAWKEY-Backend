package org.sopt.pawkey.backendapi.domain.post.application.dto.command;

import lombok.Builder;

import java.util.List;
@Builder
public record PostUpdateCommand(
        String title,
        String description,
        boolean isPublic,
        List<Long> walkImageIds,
        List<SelectedOptionsForCategory> selectedOptionsForCategories
) {
}
