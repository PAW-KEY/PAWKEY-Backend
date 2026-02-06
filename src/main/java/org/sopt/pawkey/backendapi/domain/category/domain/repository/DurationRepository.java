package org.sopt.pawkey.backendapi.domain.category.domain.repository;

import java.util.List;

import org.sopt.pawkey.backendapi.domain.category.infra.persistence.entity.DurationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DurationRepository extends JpaRepository<DurationEntity, Long> {
	@Query("SELECT s FROM DurationEntity s JOIN FETCH s.durationOptionEntity ORDER BY s.durationId ASC")
	List<DurationEntity> findAllDurationWithOptions();
}