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
		return DbtiQuestionListResponseDto.from(dbtiQueryService.getAllQuestions());
	}

	public DbtiResultResponseDto getPetDbtiResult(Long userId, Long petId) {
		var detail = dbtiQueryService.getPetDbtiResultDetail(userId, petId);

		return DbtiResultResponseDto.of(detail.result(), detail.dbtiInfo());
	}
}