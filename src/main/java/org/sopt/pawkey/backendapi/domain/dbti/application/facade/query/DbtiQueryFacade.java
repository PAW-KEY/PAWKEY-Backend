package org.sopt.pawkey.backendapi.domain.dbti.application.facade.query;

import java.util.List;

import org.sopt.pawkey.backendapi.domain.dbti.api.dto.response.DbtiQuestionListResponseDto;
import org.sopt.pawkey.backendapi.domain.dbti.api.dto.response.DbtiResultResponseDto;
import org.sopt.pawkey.backendapi.domain.dbti.application.dto.DbtiResultInfo;
import org.sopt.pawkey.backendapi.domain.dbti.application.service.DbtiQueryService;
import org.sopt.pawkey.backendapi.domain.dbti.infra.persistence.entity.DbtiEntity;
import org.sopt.pawkey.backendapi.domain.dbti.infra.persistence.entity.DbtiResultEntity;
import org.sopt.pawkey.backendapi.domain.dbti.infra.persistence.entity.DbtiTypeEntity;
import org.sopt.pawkey.backendapi.domain.pet.application.service.PetQueryService;
import org.sopt.pawkey.backendapi.domain.pet.infra.persistence.entity.PetEntity;
import org.sopt.pawkey.backendapi.domain.user.application.service.UserService;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DbtiQueryFacade {
	private final DbtiQueryService dbtiQueryService;
	private final PetQueryService petQueryService;
	private final UserService userService;

	public DbtiQuestionListResponseDto getDbtiQuestions(Long userId) {
		userService.findById(userId);
		return DbtiQuestionListResponseDto.from(dbtiQueryService.getAllQuestions());
	}

	public DbtiResultResponseDto getPetDbtiResult(Long userId, Long petId) {
		PetEntity pet = petQueryService.getPetOwnedByUser(userId, petId);
		DbtiResultEntity result = dbtiQueryService.getPetDbtiResult(pet.getPetId());
		DbtiEntity dbtiInfo = dbtiQueryService.getDbtiInfo(result.getDbtiType());
		List<DbtiTypeEntity> types = dbtiQueryService.getAllTypes();
		DbtiResultInfo info = DbtiResultInfo.of(result, dbtiInfo, types);

		return DbtiResultResponseDto.from(info);
	}
}