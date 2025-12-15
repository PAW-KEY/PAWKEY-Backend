package org.sopt.pawkey.backendapi.domain.auth.api.dto.request;

import org.sopt.pawkey.backendapi.domain.auth.domain.Provider;

public record WithdrawRequestDTO(
	Provider provider
) {
}
