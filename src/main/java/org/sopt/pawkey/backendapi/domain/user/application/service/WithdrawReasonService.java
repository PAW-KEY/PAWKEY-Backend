package org.sopt.pawkey.backendapi.domain.user.application.service;

import org.sopt.pawkey.backendapi.domain.user.api.dto.request.WithdrawReasonRequestDto;
import org.sopt.pawkey.backendapi.domain.user.domain.repository.WithdrawReasonRepository;
import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.WithdrawReasonEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WithdrawReasonService {

	private final WithdrawReasonRepository withdrawReasonRepository;

	@Transactional
	public void saveWithdrawReason(Long userId, WithdrawReasonRequestDto requestDto) {
		WithdrawReasonEntity entity = WithdrawReasonEntity.builder()
			.userId(userId)
			.reasonCode(requestDto.reasonCode())
			.build();
		withdrawReasonRepository.save(entity);
	}
}
