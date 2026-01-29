package org.sopt.pawkey.backendapi.domain.routes.infra.persistence;

import org.sopt.pawkey.backendapi.domain.home.api.dto.response.HomeInfoResponseDto;

public interface RouteQueryRepository {
	HomeInfoResponseDto getMonthlyWalkSummary(Long userId);
}
