package org.sopt.pawkey.backendapi.domain.home.application.service;

import org.sopt.pawkey.backendapi.domain.home.api.dto.response.HomeInfoResponseDto;
import org.sopt.pawkey.backendapi.domain.routes.domain.repository.RouteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HomeQueryService {
	private final RouteRepository routeRepository;

	public HomeInfoResponseDto getHomeInfo(Long userId) {
		return routeRepository.getMonthlyWalkSummary(userId);
	}
}
