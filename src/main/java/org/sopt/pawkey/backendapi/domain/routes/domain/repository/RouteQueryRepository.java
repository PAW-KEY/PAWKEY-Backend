package org.sopt.pawkey.backendapi.domain.routes.domain.repository;

import org.sopt.pawkey.backendapi.domain.homeWeather.api.dto.HomeInfoResponseDto;

public interface RouteQueryRepository {
	HomeInfoResponseDto getMonthlyWalkSummary(Long userId);
}
