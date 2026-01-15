package org.sopt.pawkey.backendapi.domain.dbti.application.facade.query;

import org.sopt.pawkey.backendapi.domain.dbti.api.dto.response.DbtiQuestionListResponseDto;
import org.sopt.pawkey.backendapi.domain.dbti.api.dto.response.DbtiResultResponseDto;
import org.sopt.pawkey.backendapi.domain.dbti.application.service.DbtiQueryService;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DbtiQueryFacade {
	private final DbtiQueryService dbtiQueryService;

	public DbtiQuestionListResponseDto getDbtiQuestions() {
		var entities = dbtiQueryService.getAllQuestions();

		// DTO 구조로 변환하여 반환
		var questions = entities.stream()
			.map(q -> new DbtiQuestionListResponseDto.QuestionDto(
				q.getId(),
				new DbtiQuestionListResponseDto.CategoryDto(q.getDbtiType().getCode(), q.getDbtiType().getName()),
				q.getContent(),
				q.getOptions().stream()
					.map(o -> new DbtiQuestionListResponseDto.OptionDto(o.getId(), o.getContent(), o.getImageUrl(),
						o.getValue()))
					.toList()
			)).toList();

		return DbtiQuestionListResponseDto.from(questions);
	}

	public DbtiResultResponseDto getPetDbtiResult(Long petId) {
		var detail = dbtiQueryService.getPetDbtiResultDetail(petId);

		return DbtiResultResponseDto.of(detail.result(), detail.dbtiInfo());
	}
}