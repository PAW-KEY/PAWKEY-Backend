package org.sopt.pawkey.backendapi.domain.dbti.infra.persistence.repository;

import java.util.Optional;

import feign.Param;
import org.sopt.pawkey.backendapi.domain.dbti.infra.persistence.entity.DbtiResultEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface SpringDataDbtiResultRepository extends JpaRepository<DbtiResultEntity, Long> {
	Optional<DbtiResultEntity> findByPet_PetId(Long petId);

	@Modifying(clearAutomatically = true)
	@Query("""
        delete from DbtiResultEntity d
        where d.pet.user.userId = :userId
    """)
	void deleteByUserId(@Param("userId") Long userId);
}