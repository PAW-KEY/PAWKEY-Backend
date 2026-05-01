package org.sopt.pawkey.backendapi.domain.user.domain.repository;

import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.WithdrawReasonEntity;

public interface WithdrawReasonRepository {
	WithdrawReasonEntity save(WithdrawReasonEntity entity);
}
