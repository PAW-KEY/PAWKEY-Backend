package org.sopt.pawkey.backendapi.domain.dbti.application.facade;

import org.sopt.pawkey.backendapi.domain.dbti.api.dto.request.DbtiSubmitRequestDto;
import org.sopt.pawkey.backendapi.domain.dbti.api.dto.response.DbtiResultResponseDto;
import org.sopt.pawkey.backendapi.domain.dbti.application.service.DbtiCommandService;
import org.sopt.pawkey.backendapi.domain.dbti.application.service.DbtiQueryService;
import org.sopt.pawkey.backendapi.domain.dbti.infra.persistence.entity.DbtiEntity;
import org.sopt.pawkey.backendapi.domain.dbti.infra.persistence.entity.DbtiResultEntity;
import org.sopt.pawkey.backendapi.domain.pet.application.service.PetQueryService;
import org.sopt.pawkey.backendapi.domain.pet.infra.persistence.entity.PetEntity;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DbtiCommandFacade {

	private final DbtiCommandService dbtiCommandService;

	private final DbtiQueryService dbtiQueryService;

	private final PetQueryService petQueryService;

	public DbtiResultResponseDto submitDbtiTest(Long userId, Long petId, DbtiSubmitRequestDto request) {
		PetEntity pet = petQueryService.getPetOwnedByUser(userId, petId);

		DbtiResultEntity result = dbtiCommandService.calculateAndSave(pet, request);

		DbtiEntity dbtiInfo = dbtiQueryService.getDbtiInfo(result.getDbtiType());

		return DbtiResultResponseDto.of(result, dbtiInfo);
	}
}