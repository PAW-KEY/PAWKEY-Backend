package org.sopt.pawkey.backendapi.domain.user.infra.persistence;

import java.util.Optional;

import org.sopt.pawkey.backendapi.domain.user.domain.repository.SocialAccountRepository;
import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.SocialAccountEntity;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class SocialAccountRepositoryImpl implements SocialAccountRepository {
	private final SpringDataSocialAccountRepository jpaRepository;
	@Override
	public Optional<SocialAccountEntity> findByPlatformAndPlatformUserId(String platform, String platformUserId) {
		return jpaRepository.findByPlatformAndPlatformUserId(platform, platformUserId);
	}

	@Override
	public SocialAccountEntity save(SocialAccountEntity socialAccount) {
		return jpaRepository.save(socialAccount);
	}
}
