package org.sopt.pawkey.backendapi.domain.dbti.infra.persistence.repository;

import lombok.RequiredArgsConstructor;

import org.sopt.pawkey.backendapi.domain.dbti.domain.model.DbtiType;
import org.sopt.pawkey.backendapi.domain.dbti.domain.repository.DbtiRepository;
import org.sopt.pawkey.backendapi.domain.dbti.infra.persistence.entity.DbtiEntity;
import org.sopt.pawkey.backendapi.domain.dbti.infra.persistence.entity.DbtiOptionEntity;
import org.sopt.pawkey.backendapi.domain.dbti.infra.persistence.entity.DbtiQuestionEntity;
import org.sopt.pawkey.backendapi.domain.dbti.infra.persistence.entity.DbtiTypeEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class DbtiRepositoryImpl implements DbtiRepository {

	private final SpringDataDbtiQuestionRepository springDataDbtiQuestionRepository;
	private final SpringDataDbtiOptionRepository optionRepository;
	private final SpringDataDbtiRepository dbtiRepository;

	private final SpringDataDbtiTypeRepository dbtiTypeRepository;

	@Override
	public List<DbtiQuestionEntity> findAllQuestionsWithDetails() {
		return springDataDbtiQuestionRepository.findAllWithDetails();
	}

	@Override
	public List<DbtiOptionEntity> findAllOptionsByIds(List<Long> optionIds) {
		return optionRepository.findAllById(optionIds);
	}

	@Override
	public Optional<DbtiEntity> findDbtiByType(DbtiType type) {
		return dbtiRepository.findById(type);
	}

	@Override
	public List<DbtiTypeEntity> findAllTypes() {
		return dbtiTypeRepository.findAll();
	}
}