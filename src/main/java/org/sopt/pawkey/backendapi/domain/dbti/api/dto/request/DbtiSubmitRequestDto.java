package org.sopt.pawkey.backendapi.domain.dbti.api.dto.request;

import java.util.List;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record DbtiSubmitRequestDto(
	@NotNull(message = "답변 목록은 필수입니다.")
	@Size(min = 9, max = 9, message = "9개의 답변이 모두 필요합니다.")
	List<Long> optionIds
) {
}