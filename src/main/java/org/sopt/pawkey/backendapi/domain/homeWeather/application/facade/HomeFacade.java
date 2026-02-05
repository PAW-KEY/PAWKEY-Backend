package org.sopt.pawkey.backendapi.domain.homeWeather.application.facade;

import org.sopt.pawkey.backendapi.domain.homeWeather.api.dto.HomeInfoResponseDto;
import org.sopt.pawkey.backendapi.domain.homeWeather.application.service.HomeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HomeFacade {
	private final HomeService homeService;

	public HomeInfoResponseDto getHomeInfo(Long userId) {
		return homeService.getHomeInfo(userId);
	}
}