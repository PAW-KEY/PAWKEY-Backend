package org.sopt.pawkey.backendapi.domain.post.api.dto.request;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.sopt.pawkey.backendapi.domain.post.application.dto.command.PostUpdateCommand;
import org.sopt.pawkey.backendapi.domain.post.application.dto.command.SelectedOptionsForCategory;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class PostUpdateRequestDto {

    @NotBlank
    private final String title;

    @NotBlank
    private final String description;

    private final boolean isPublic;

    // 산책 중 촬영한 이미지들만 수정 가능
    @Size(max = 4)
    private final List<Long> walkImageIds;

    @NotEmpty
    private final List<@Valid SelectedOptionsForCategory> selectedOptionsForCategories;

    public PostUpdateCommand toCommand() {
        return PostUpdateCommand.builder()
                .title(title)
                .description(description)
                .isPublic(isPublic)
                .walkImageIds(walkImageIds)
                .selectedOptionsForCategories(selectedOptionsForCategories)
                .build();
    }
}
