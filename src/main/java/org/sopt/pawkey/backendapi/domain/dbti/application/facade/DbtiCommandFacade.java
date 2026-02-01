package org.sopt.pawkey.backendapi.domain.dbti.application.facade;

import org.sopt.pawkey.backendapi.domain.dbti.api.dto.request.DbtiSubmitRequestDto;
import org.sopt.pawkey.backendapi.domain.dbti.api.dto.response.DbtiResultResponseDto;
import org.sopt.pawkey.backendapi.domain.dbti.application.dto.DbtiResultInfo;
import org.sopt.pawkey.backendapi.domain.dbti.application.service.DbtiCommandService;
import org.sopt.pawkey.backendapi.domain.pet.application.service.PetQueryService;
import org.sopt.pawkey.backendapi.domain.pet.infra.persistence.entity.PetEntity;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DbtiCommandFacade {

	private final DbtiCommandService dbtiCommandService;

	private final PetQueryService petQueryService;

	public DbtiResultResponseDto submitDbtiTest(Long userId, Long petId, DbtiSubmitRequestDto request) {
		PetEntity pet = petQueryService.getPetOwnedByUser(userId, petId);

		DbtiResultInfo info = dbtiCommandService.calculateAndSave(pet, request);

		return DbtiResultResponseDto.of(info.result(), info.dbtiInfo(), info.types());
	}
}