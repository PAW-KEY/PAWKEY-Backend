package org.sopt.pawkey.backendapi.domain.routes.infra.persistence;

import org.sopt.pawkey.backendapi.domain.homeWeather.api.dto.HomeInfoResponseDto;

public interface RouteQueryRepository {
	HomeInfoResponseDto getMonthlyWalkSummary(Long userId);
}
