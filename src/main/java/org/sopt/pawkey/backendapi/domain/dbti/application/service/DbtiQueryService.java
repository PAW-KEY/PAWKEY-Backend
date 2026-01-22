package org.sopt.pawkey.backendapi.domain.dbti.application.service;

import java.util.List;

import org.sopt.pawkey.backendapi.domain.dbti.application.dto.DbtiResultDetailVo;
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
import org.sopt.pawkey.backendapi.domain.user.exception.UserBusinessException;
import org.sopt.pawkey.backendapi.domain.user.exception.UserErrorCode;
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

	public DbtiResultDetailVo getPetDbtiResultDetail(Long userId, Long petId) {

		PetEntity pet = petRepository.findById(petId)
			.orElseThrow(() -> new PetBusinessException(PetErrorCode.PET_NOT_FOUND));

		if (!pet.getUser().getUserId().equals(userId)) {
			throw new UserBusinessException(UserErrorCode.USER_NOT_FOUND);
		}

		DbtiResultEntity result = resultRepository.findByPetId(petId)
			.orElseThrow(() -> new UserBusinessException(UserErrorCode.USER_PET_NOT_REGISTERED));

		DbtiEntity dbtiInfo = dbtiRepository.findDbtiByType(result.getDbtiType())
			.orElseThrow(() -> new DbtiBusinessException(DbtiErrorCode.DBTI_NOT_FOUND));

		return new org.sopt.pawkey.backendapi.domain.dbti.application.dto.DbtiResultDetailVo(result, dbtiInfo);
	}
}
