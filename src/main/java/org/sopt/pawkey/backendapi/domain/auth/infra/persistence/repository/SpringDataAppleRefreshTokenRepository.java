package org.sopt.pawkey.backendapi.domain.auth.infra.persistence.repository;

import org.sopt.pawkey.backendapi.domain.auth.infra.persistence.entity.AppleRefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataAppleRefreshTokenRepository extends JpaRepository<AppleRefreshTokenEntity, Long> {
}
