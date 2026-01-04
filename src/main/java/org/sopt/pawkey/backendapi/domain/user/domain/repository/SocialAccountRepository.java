package org.sopt.pawkey.backendapi.domain.user.domain.repository;

import java.util.Optional;

import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.SocialAccountEntity;

public interface SocialAccountRepository {

	Optional<SocialAccountEntity> findByPlatformAndPlatformUserId(String platform, String platformUserId);

	SocialAccountEntity save(SocialAccountEntity socialAccount);

	Optional<SocialAccountEntity> findByUser_UserIdAndPlatform(Long userId, String platform);

	Optional<SocialAccountEntity> findByUser_UserId(Long userId);
}
