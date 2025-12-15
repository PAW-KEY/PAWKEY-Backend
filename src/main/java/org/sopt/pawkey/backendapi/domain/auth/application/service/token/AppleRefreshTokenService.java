package org.sopt.pawkey.backendapi.domain.auth.application.service.token;

import java.util.Optional;

import org.sopt.pawkey.backendapi.domain.auth.domain.repository.AppleRefreshTokenRepository;
import org.sopt.pawkey.backendapi.domain.auth.infra.persistence.entity.AppleRefreshTokenEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AppleRefreshTokenService {

	private final AppleRefreshTokenRepository repository;

	@Transactional
	public void saveOrUpdate(Long userId, String refreshToken) {
		repository.save(
			AppleRefreshTokenEntity.builder()
				.userId(userId)
				.refreshToken(refreshToken)
				.build()
		);
	}

	public Optional<String> find(Long userId) {
		return repository.findById(userId)
			.map(AppleRefreshTokenEntity::getRefreshToken);
	}

	@Transactional
	public void delete(Long userId) {
		repository.deleteById(userId);
	}
}