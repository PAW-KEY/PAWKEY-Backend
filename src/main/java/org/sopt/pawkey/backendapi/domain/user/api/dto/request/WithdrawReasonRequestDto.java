package org.sopt.pawkey.backendapi.domain.user.api.dto.request;

import org.sopt.pawkey.backendapi.domain.user.domain.WithdrawReasonCode;

import jakarta.validation.constraints.NotNull;

public record WithdrawReasonRequestDto(
	@NotNull(message = "탈퇴 사유 코드는 필수값입니다.")
	WithdrawReasonCode reasonCode
) {
}
