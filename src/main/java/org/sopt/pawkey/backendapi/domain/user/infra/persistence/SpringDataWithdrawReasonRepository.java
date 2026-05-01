package org.sopt.pawkey.backendapi.domain.user.infra.persistence;

import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.WithdrawReasonEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataWithdrawReasonRepository extends JpaRepository<WithdrawReasonEntity, Long> {
}
