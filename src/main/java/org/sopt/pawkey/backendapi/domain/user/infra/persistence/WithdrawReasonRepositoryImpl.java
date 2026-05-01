package org.sopt.pawkey.backendapi.domain.user.infra.persistence;

import org.sopt.pawkey.backendapi.domain.user.domain.repository.WithdrawReasonRepository;
import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.WithdrawReasonEntity;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class WithdrawReasonRepositoryImpl implements WithdrawReasonRepository {

	private final SpringDataWithdrawReasonRepository springDataWithdrawReasonRepository;

	@Override
	public WithdrawReasonEntity save(WithdrawReasonEntity entity) {
		return springDataWithdrawReasonRepository.save(entity);
	}
}
