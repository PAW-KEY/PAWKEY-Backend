package org.sopt.pawkey.backendapi.domain.dbti.domain.repository;

import org.sopt.pawkey.backendapi.domain.dbti.infra.persistence.entity.DbtiQuestionEntity;
import java.util.List;

public interface DbtiRepository {
	List<DbtiQuestionEntity> findAllQuestionsWithDetails();
}