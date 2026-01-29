package org.sopt.pawkey.backendapi.domain.home.application.facade;

import org.sopt.pawkey.backendapi.domain.home.api.dto.response.HomeInfoResponseDto;
import org.sopt.pawkey.backendapi.domain.home.application.service.HomeQueryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HomeQueryFacade {
	private final HomeQueryService homeQueryService;

	public HomeInfoResponseDto getHomeInfo(Long userId) {
		return homeQueryService.getHomeInfo(userId);
	}
}