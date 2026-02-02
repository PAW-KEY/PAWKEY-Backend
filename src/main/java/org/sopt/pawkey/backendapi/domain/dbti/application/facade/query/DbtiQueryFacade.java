package org.sopt.pawkey.backendapi.domain.dbti.application.facade.query;

import org.sopt.pawkey.backendapi.domain.dbti.api.dto.response.DbtiQuestionListResponseDto;
import org.sopt.pawkey.backendapi.domain.dbti.api.dto.response.DbtiResultResponseDto;
import org.sopt.pawkey.backendapi.domain.dbti.application.service.DbtiQueryService;
import org.sopt.pawkey.backendapi.domain.dbti.infra.persistence.entity.DbtiEntity;
import org.sopt.pawkey.backendapi.domain.dbti.infra.persistence.entity.DbtiResultEntity;
import org.sopt.pawkey.backendapi.domain.pet.application.service.PetQueryService;
import org.sopt.pawkey.backendapi.domain.pet.infra.persistence.entity.PetEntity;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DbtiQueryFacade {
	private final DbtiQueryService dbtiQueryService;
	private final PetQueryService petQueryService;

	public DbtiQuestionListResponseDto getDbtiQuestions() {
		return DbtiQuestionListResponseDto.from(dbtiQueryService.getAllQuestions());
	}

	public DbtiResultResponseDto getPetDbtiResult(Long userId, Long petId) {
		PetEntity pet = petQueryService.getPetOwnedByUser(userId, petId);
		DbtiResultEntity result = dbtiQueryService.getPetDbtiResult(pet.getPetId());
		DbtiEntity dbtiInfo = dbtiQueryService.getDbtiInfo(result.getDbtiType());

		return DbtiResultResponseDto.of(result, dbtiInfo);
	}
}