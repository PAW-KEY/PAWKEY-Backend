package org.sopt.pawkey.backendapi.domain.dbti.application.service;

import java.util.List;

import org.sopt.pawkey.backendapi.domain.dbti.domain.model.DbtiType;
import org.sopt.pawkey.backendapi.domain.dbti.domain.repository.DbtiRepository;
import org.sopt.pawkey.backendapi.domain.dbti.domain.repository.DbtiResultRepository;
import org.sopt.pawkey.backendapi.domain.dbti.exception.DbtiBusinessException;
import org.sopt.pawkey.backendapi.domain.dbti.exception.DbtiErrorCode;
import org.sopt.pawkey.backendapi.domain.dbti.infra.persistence.entity.DbtiEntity;
import org.sopt.pawkey.backendapi.domain.dbti.infra.persistence.entity.DbtiQuestionEntity;
import org.sopt.pawkey.backendapi.domain.dbti.infra.persistence.entity.DbtiResultEntity;
import org.sopt.pawkey.backendapi.domain.pet.domain.repository.PetRepository;
import org.sopt.pawkey.backendapi.domain.pet.exception.PetBusinessException;
import org.sopt.pawkey.backendapi.domain.pet.exception.PetErrorCode;
import org.sopt.pawkey.backendapi.domain.pet.infra.persistence.entity.PetEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DbtiQueryService {
	private final DbtiRepository dbtiRepository;

	public List<DbtiQuestionEntity> getAllQuestions() {
		return dbtiRepository.findAllQuestionsWithDetails();
	}

	private final DbtiResultRepository resultRepository;
	private final PetRepository petRepository;

	public DbtiResultEntity getPetDbtiResult(Long petId) {
		return resultRepository.findByPetId(petId)
			.orElseThrow(() -> new DbtiBusinessException(DbtiErrorCode.DBTI_RESULT_NOT_FOUND));
	}

	public DbtiEntity getDbtiInfo(DbtiType type) {
		return dbtiRepository.findDbtiByType(type)
			.orElseThrow(() -> new DbtiBusinessException(DbtiErrorCode.DBTI_NOT_FOUND));
	}
}
