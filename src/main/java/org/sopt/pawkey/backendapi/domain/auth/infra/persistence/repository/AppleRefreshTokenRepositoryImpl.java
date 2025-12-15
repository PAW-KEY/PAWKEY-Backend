package org.sopt.pawkey.backendapi.domain.auth.infra.persistence.repository;

import java.util.Optional;

import org.sopt.pawkey.backendapi.domain.auth.domain.repository.AppleRefreshTokenRepository;
import org.sopt.pawkey.backendapi.domain.auth.infra.persistence.entity.AppleRefreshTokenEntity;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class AppleRefreshTokenRepositoryImpl implements AppleRefreshTokenRepository {
	private final SpringDataAppleRefreshTokenRepository jpaRepository;

	@Override
	public Optional<AppleRefreshTokenEntity> findById(Long userId) {
		return jpaRepository.findById(userId);
	}

	@Override
	public void deleteById(Long userId) {
		jpaRepository.deleteById(userId);
	}

	@Override
	public AppleRefreshTokenEntity save(AppleRefreshTokenEntity entity) {
		return jpaRepository.save(entity);
	}
}
