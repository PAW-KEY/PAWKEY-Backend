package org.sopt.pawkey.backendapi.domain.post.api.dto.request;

import java.util.List;

import org.sopt.pawkey.backendapi.domain.post.application.dto.command.PostRegisterCommand;
import org.sopt.pawkey.backendapi.domain.post.application.dto.command.SelectedOptionsForCategory;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
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

	private final boolean isPublic;

	@NotEmpty(message = "카테고리 선택은 비어 있을 수 없습니다.")
	private final List<@Valid SelectedOptionsForCategory> selectedOptionsForCategories;

	@NotNull(message = "루트 ID는 필수입니다.")
	private final Long routeId;

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
			.build();
	}
}


