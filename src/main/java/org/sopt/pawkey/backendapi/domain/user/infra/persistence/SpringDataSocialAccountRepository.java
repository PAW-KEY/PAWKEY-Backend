package org.sopt.pawkey.backendapi.domain.user.infra.persistence;

import java.util.Optional;

import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.SocialAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataSocialAccountRepository extends JpaRepository<SocialAccountEntity, Long> {

	Optional<SocialAccountEntity> findByPlatformAndPlatformUserId(String platform, String platformUserId);

}
