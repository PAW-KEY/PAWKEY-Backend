package org.sopt.pawkey.backendapi.domain.dbti.api.dto.response;

import java.util.List;

public record DbtiQuestionListResponseDto(List<QuestionDto> questions) {
	public static DbtiQuestionListResponseDto from(List<QuestionDto> questions) {
		return new DbtiQuestionListResponseDto(questions);
	}

	public record QuestionDto(Long id, CategoryDto category, String content, List<OptionDto> options) {}
	public record CategoryDto(String code, String name) {}
	public record OptionDto(Long id, String content, String imageUrl, String value) {}
}