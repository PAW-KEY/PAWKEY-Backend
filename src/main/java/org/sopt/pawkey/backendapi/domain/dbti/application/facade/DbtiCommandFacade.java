package org.sopt.pawkey.backendapi.domain.dbti.application.facade;

import org.sopt.pawkey.backendapi.domain.dbti.api.dto.request.DbtiSubmitRequestDto;
import org.sopt.pawkey.backendapi.domain.dbti.api.dto.response.DbtiResultResponseDto;
import org.sopt.pawkey.backendapi.domain.dbti.application.service.DbtiCommandService;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DbtiCommandFacade {

	private final DbtiCommandService dbtiCommandService;

	public DbtiResultResponseDto submitDbtiTest(Long petId, DbtiSubmitRequestDto request) {
		var detail = dbtiCommandService.calculateSaveAndGetDetail(petId, request);

		return DbtiResultResponseDto.of(detail.result(), detail.dbtiInfo());
	}
}