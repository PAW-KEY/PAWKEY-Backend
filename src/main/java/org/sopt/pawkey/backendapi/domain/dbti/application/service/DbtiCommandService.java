package org.sopt.pawkey.backendapi.domain.dbti.application.service;

import java.util.List;

import org.sopt.pawkey.backendapi.domain.dbti.api.dto.request.DbtiSubmitRequestDto;
import org.sopt.pawkey.backendapi.domain.dbti.application.dto.DbtiResultDetailVo;
import org.sopt.pawkey.backendapi.domain.dbti.domain.model.DbtiType;
import org.sopt.pawkey.backendapi.domain.dbti.domain.repository.DbtiRepository;
import org.sopt.pawkey.backendapi.domain.dbti.domain.repository.DbtiResultRepository;
import org.sopt.pawkey.backendapi.domain.dbti.exception.DbtiBusinessException;
import org.sopt.pawkey.backendapi.domain.dbti.exception.DbtiErrorCode;
import org.sopt.pawkey.backendapi.domain.dbti.infra.persistence.entity.DbtiEntity;
import org.sopt.pawkey.backendapi.domain.dbti.infra.persistence.entity.DbtiOptionEntity;
import org.sopt.pawkey.backendapi.domain.dbti.infra.persistence.entity.DbtiResultEntity;
import org.sopt.pawkey.backendapi.domain.pet.domain.repository.PetRepository;
import org.sopt.pawkey.backendapi.domain.pet.exception.PetBusinessException;
import org.sopt.pawkey.backendapi.domain.pet.exception.PetErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class DbtiCommandService {
	private final DbtiRepository dbtiRepository;
	private final DbtiResultRepository resultRepository;

	private final PetRepository petRepository;

	public DbtiResultDetailVo calculateSaveAndGetDetail(Long petId, DbtiSubmitRequestDto request) {

		if (!petRepository.existsById(petId)) {
			throw new PetBusinessException(PetErrorCode.PET_NOT_FOUND);
		}

		List<DbtiOptionEntity> selectedOptions = dbtiRepository.findAllOptionsByIds(request.optionIds());

		int eiScore = (int)selectedOptions.stream().filter(o -> o.getValue().equals("E")).count();
		int psScore = (int)selectedOptions.stream().filter(o -> o.getValue().equals("P")).count();
		int rfScore = (int)selectedOptions.stream().filter(o -> o.getValue().equals("R")).count();

		String dbtiType = DbtiType.determine(eiScore, psScore, rfScore);

		DbtiResultEntity result = resultRepository.save(DbtiResultEntity.builder()
			.petId(petId)
			.dbtiType(dbtiType)
			.eiScore(eiScore)
			.psScore(psScore)
			.rfScore(rfScore)
			.build());

		DbtiEntity dbtiInfo = dbtiRepository.findDbtiByType(dbtiType)
			.orElseThrow(() -> new DbtiBusinessException(DbtiErrorCode.DBTI_NOT_FOUND));

		return new DbtiResultDetailVo(result, dbtiInfo);
	}
}