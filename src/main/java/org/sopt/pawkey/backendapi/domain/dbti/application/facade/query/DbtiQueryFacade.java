package org.sopt.pawkey.backendapi.domain.dbti.application.facade.query;

import org.sopt.pawkey.backendapi.domain.dbti.api.dto.response.DbtiQuestionListResponseDto;
import org.sopt.pawkey.backendapi.domain.dbti.api.dto.response.DbtiResultResponseDto;
import org.sopt.pawkey.backendapi.domain.dbti.application.service.DbtiQueryService;
import org.sopt.pawkey.backendapi.domain.dbti.infra.persistence.entity.DbtiEntity;
import org.sopt.pawkey.backendapi.domain.dbti.infra.persistence.entity.DbtiResultEntity;
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
		DbtiResultEntity result = dbtiQueryService.getPetDbtiResult(userId, petId);
		DbtiEntity dbtiInfo = dbtiQueryService.getDbtiInfo(result.getDbtiType());

		return DbtiResultResponseDto.of(result, dbtiInfo);
	}
}