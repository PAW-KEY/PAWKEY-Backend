package org.sopt.pawkey.backendapi.domain.user.api.dto.result;

import lombok.Builder;

@Builder
public record UserCreationResult(
	Long userId,
	boolean isNewUser
) {
}
