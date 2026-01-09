package org.sopt.pawkey.backendapi.domain.dbti.infra.persistence.repository;

import java.util.List;

import org.sopt.pawkey.backendapi.domain.dbti.infra.persistence.entity.DbtiQuestionEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SpringDataDbtiQuestionRepository extends JpaRepository<DbtiQuestionEntity, Long> {
	@EntityGraph(attributePaths = {"dbtiType", "options"})
	@Query("SELECT q FROM DbtiQuestionEntity q ORDER BY q.sequence ASC")
	List<DbtiQuestionEntity> findAllWithDetails();
}