package org.sopt.pawkey.backendapi.domain.user.domain.repository;

import java.util.Optional;

import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.UserEntity;

public interface UserRepository {

	UserEntity save(final UserEntity user);

	Optional<UserEntity> findById(Long id);

	void deleteById(Long userId);

	boolean existsByName(String name);

	void saveAndFlush(UserEntity user);
}
