package org.sopt.pawkey.backendapi.domain.homeWeather.application.service;

import org.sopt.pawkey.backendapi.domain.homeWeather.api.dto.HomeInfoResponseDto;
import org.sopt.pawkey.backendapi.domain.routes.domain.repository.RouteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HomeService {
	private final RouteRepository routeRepository;

	public HomeInfoResponseDto getHomeInfo(Long userId) {
		return routeRepository.getMonthlyWalkSummary(userId);
	}
}
