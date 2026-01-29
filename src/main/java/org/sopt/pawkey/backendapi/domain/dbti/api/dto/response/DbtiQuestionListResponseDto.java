package org.sopt.pawkey.backendapi.domain.dbti.api.dto.response;

import java.util.List;

import org.sopt.pawkey.backendapi.domain.dbti.infra.persistence.entity.DbtiQuestionEntity;

public record DbtiQuestionListResponseDto(List<QuestionDto> questions) {
	public static DbtiQuestionListResponseDto from(List<DbtiQuestionEntity> entities) {
		var questions = entities.stream()
			.map(q -> new QuestionDto(
				q.getId(),
				new CategoryDto(q.getDbtiType().getCode(), q.getDbtiType().getName()),
				q.getContent(),
				q.getOptions().stream()
					.map(o -> new OptionDto(o.getId(), o.getContent(), o.getImageUrl(), o.getValue()))
					.toList()
			)).toList();
		return new DbtiQuestionListResponseDto(questions);
	}

	public record QuestionDto(Long id, CategoryDto category, String content, List<OptionDto> options) {
	}

	public record CategoryDto(String code, String name) {
	}

	public record OptionDto(Long id, String content, String imageUrl, String value) {
	}
}