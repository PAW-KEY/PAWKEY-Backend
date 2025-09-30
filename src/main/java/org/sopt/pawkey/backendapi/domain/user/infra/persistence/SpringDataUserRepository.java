package org.sopt.pawkey.backendapi.domain.user.infra.persistence;

import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataUserRepository extends JpaRepository<UserEntity, Long> {
	UserEntity getByUserId(Long userId);
}
