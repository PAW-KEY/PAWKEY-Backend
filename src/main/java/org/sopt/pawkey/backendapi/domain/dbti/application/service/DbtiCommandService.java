package org.sopt.pawkey.backendapi.domain.dbti.application.service;

import java.util.List;

import org.sopt.pawkey.backendapi.domain.dbti.api.dto.request.DbtiSubmitRequestDto;
import org.sopt.pawkey.backendapi.domain.dbti.application.dto.DbtiResultInfo;
import org.sopt.pawkey.backendapi.domain.dbti.domain.model.DbtiType;
import org.sopt.pawkey.backendapi.domain.dbti.domain.repository.DbtiRepository;
import org.sopt.pawkey.backendapi.domain.dbti.domain.repository.DbtiResultRepository;
import org.sopt.pawkey.backendapi.domain.dbti.exception.DbtiBusinessException;
import org.sopt.pawkey.backendapi.domain.dbti.exception.DbtiErrorCode;
import org.sopt.pawkey.backendapi.domain.dbti.infra.persistence.entity.DbtiEntity;
import org.sopt.pawkey.backendapi.domain.dbti.infra.persistence.entity.DbtiOptionEntity;
import org.sopt.pawkey.backendapi.domain.dbti.infra.persistence.entity.DbtiResultEntity;
import org.sopt.pawkey.backendapi.domain.dbti.infra.persistence.entity.DbtiTypeEntity;
import org.sopt.pawkey.backendapi.domain.pet.infra.persistence.entity.PetEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class DbtiCommandService {
	private final DbtiRepository dbtiRepository;
	private final DbtiResultRepository resultRepository;

	private static final int EXPECTED_OPTION_COUNT = 9;

	public DbtiResultInfo calculateAndSave(PetEntity pet, DbtiSubmitRequestDto request) {

		if (request.optionIds() == null || request.optionIds().size() != EXPECTED_OPTION_COUNT) {
			throw new DbtiBusinessException(DbtiErrorCode.INVALID_OPTION_COUNT);
		}

		List<DbtiOptionEntity> selectedOptions = dbtiRepository.findAllOptionsByIds(request.optionIds());

		if (selectedOptions.size() != request.optionIds().size()) {
			throw new DbtiBusinessException(DbtiErrorCode.INVALID_OPTION_IDS);
		}

		int eiScore = (int)selectedOptions.stream().filter(o -> o.getValue().equals("E")).count();
		int psScore = (int)selectedOptions.stream().filter(o -> o.getValue().equals("P")).count();
		int rfScore = (int)selectedOptions.stream().filter(o -> o.getValue().equals("R")).count();

		DbtiType dbtiType = DbtiType.determine(eiScore, psScore, rfScore);

		DbtiResultEntity result = resultRepository.findByPetId(pet.getPetId())
			.map(existingResult -> {
				existingResult.updateResult(dbtiType, eiScore, psScore, rfScore);
				return existingResult;
			})
			.orElseGet(() -> resultRepository.save(DbtiResultEntity.builder()
				.pet(pet)
				.dbtiType(dbtiType)
				.eiScore(eiScore)
				.psScore(psScore)
				.rfScore(rfScore)
				.build()));

		DbtiEntity dbtiInfo = dbtiRepository.findDbtiByType(dbtiType)
			.orElseThrow(() -> new DbtiBusinessException(DbtiErrorCode.DBTI_NOT_FOUND));

		List<DbtiTypeEntity> types = dbtiRepository.findAllTypes();

		return new DbtiResultInfo(result, dbtiInfo, types);
	}
}