package org.sopt.pawkey.backendapi.domain.dbti.infra.persistence.repository;

import lombok.RequiredArgsConstructor;
import org.sopt.pawkey.backendapi.domain.dbti.domain.repository.DbtiRepository;
import org.sopt.pawkey.backendapi.domain.dbti.infra.persistence.entity.DbtiQuestionEntity;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class DbtiRepositoryImpl implements DbtiRepository {

	private final SpringDataDbtiQuestionRepository springDataDbtiQuestionRepository;

	@Override
	public List<DbtiQuestionEntity> findAllQuestionsWithDetails() {
		return springDataDbtiQuestionRepository.findAllWithDetails();
	}
}