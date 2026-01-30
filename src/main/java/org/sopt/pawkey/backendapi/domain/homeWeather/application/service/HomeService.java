package org.sopt.pawkey.backendapi.domain.homeWeather.application.service;

import org.sopt.pawkey.backendapi.domain.homeWeather.api.dto.HomeInfoResponseDto;
import org.sopt.pawkey.backendapi.domain.routes.domain.repository.RouteRepository;
import org.sopt.pawkey.backendapi.domain.user.domain.repository.UserRepository;
import org.sopt.pawkey.backendapi.domain.user.exception.UserErrorCode;
import org.sopt.pawkey.backendapi.global.exception.BusinessException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HomeService {
	private final RouteRepository routeRepository;
	private final UserRepository userRepository;

	@Cacheable(value = "homeInfo", key = "#userId")
	public HomeInfoResponseDto getHomeInfo(Long userId) {
		userRepository.findById(userId).orElseThrow(() -> new BusinessException(UserErrorCode.USER_NOT_FOUND));
		return routeRepository.getMonthlyWalkSummary(userId);
	}
}
