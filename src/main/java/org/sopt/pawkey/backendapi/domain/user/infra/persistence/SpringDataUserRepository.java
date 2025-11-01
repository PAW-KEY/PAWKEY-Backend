package org.sopt.pawkey.backendapi.domain.user.infra.persistence;

import java.util.Optional;

import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataUserRepository extends JpaRepository<UserEntity, Long> {

	@Override
	@EntityGraph(attributePaths = {
		"region",
		"region.parent",
		"petEntityList",
		"reviewEntityList",
		"postLikeEntityList"
	})
	Optional<UserEntity> findById(Long id);
}
