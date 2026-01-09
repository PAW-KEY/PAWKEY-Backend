package org.sopt.pawkey.backendapi.domain.dbti.application.service;

import java.util.List;

import org.sopt.pawkey.backendapi.domain.dbti.domain.repository.DbtiRepository;
import org.sopt.pawkey.backendapi.domain.dbti.infra.persistence.entity.DbtiQuestionEntity;
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
}
