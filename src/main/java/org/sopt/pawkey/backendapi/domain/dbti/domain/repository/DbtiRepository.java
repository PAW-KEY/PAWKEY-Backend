package org.sopt.pawkey.backendapi.domain.dbti.domain.repository;

import org.sopt.pawkey.backendapi.domain.dbti.domain.model.DbtiType;
import org.sopt.pawkey.backendapi.domain.dbti.infra.persistence.entity.DbtiEntity;
import org.sopt.pawkey.backendapi.domain.dbti.infra.persistence.entity.DbtiOptionEntity;
import org.sopt.pawkey.backendapi.domain.dbti.infra.persistence.entity.DbtiQuestionEntity;

import java.util.List;
import java.util.Optional;

public interface DbtiRepository {
	List<DbtiQuestionEntity> findAllQuestionsWithDetails();

	List<DbtiOptionEntity> findAllOptionsByIds(List<Long> optionIds);

	Optional<DbtiEntity> findDbtiByType(DbtiType type);
}