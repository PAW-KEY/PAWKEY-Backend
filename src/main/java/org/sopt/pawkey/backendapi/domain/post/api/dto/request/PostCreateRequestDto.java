package org.sopt.pawkey.backendapi.domain.post.api.dto.request;

import java.util.List;

import org.sopt.pawkey.backendapi.domain.post.application.dto.command.PostRegisterCommand;
import org.sopt.pawkey.backendapi.domain.post.application.dto.command.SelectedOptionsForCategory;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PostCreateRequestDto {

	@NotBlank(message = "게시물 제목은 비어 있을 수 없습니다.")
	@Size(max = 100, message = "게시물 제목은 100자 이내여야 합니다.")
	private final String title;

	@NotBlank(message = "게시물 본문은 비어 있을 수 없습니다.")
	@Size(max = 1000, message = "게시물 본문은 1000자 이내여야 합니다.")
	private final String description;

	@NotNull(message = "게시물 공개 여부는 필수입니다.")
	private final boolean isPublic;

	@NotEmpty(message = "카테고리 선택은 비어 있을 수 없습니다.")
	private final List<@Valid SelectedOptionsForCategory> selectedOptionsForCategories;

	@NotNull(message = "루트 ID는 필수입니다.")
	private final Long routeId;

	@NotNull(message = "루트 이미지 ID는 필수입니다.")
	private final Long routeImageId;

	@Size(max = 4, message = "산책 이미지는 최대 4장까지 등록할 수 있습니다.")
	private final List<Long> walkImageIds;


	public boolean getIsPublic() {
		return isPublic;
	}

	public PostRegisterCommand toCommand() {
		return PostRegisterCommand.builder()
				.title(this.title)
				.description(this.description)
				.isPublic(this.isPublic)
				.selectedOptionsForCategories(this.selectedOptionsForCategories)
				.routeId(this.routeId)
				.routeImageId(this.routeImageId)
				.walkImageIds(this.walkImageIds != null ? this.walkImageIds : List.of())
				.build();
	}
}


