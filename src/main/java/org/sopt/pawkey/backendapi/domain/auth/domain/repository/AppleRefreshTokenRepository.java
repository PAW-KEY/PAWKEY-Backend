package org.sopt.pawkey.backendapi.domain.auth.domain.repository;

import java.util.Optional;

import org.sopt.pawkey.backendapi.domain.auth.infra.persistence.entity.AppleRefreshTokenEntity;

public interface AppleRefreshTokenRepository {
	Optional<AppleRefreshTokenEntity> findById(Long userId);
	void deleteById(Long userId);
	AppleRefreshTokenEntity save(AppleRefreshTokenEntity entity);
}
