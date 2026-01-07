package org.sopt.pawkey.backendapi.domain.routes.application.facade.command;

import org.sopt.pawkey.backendapi.domain.user.application.service.UserService;
import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.UserEntity;
import org.sopt.pawkey.backendapi.domain.weather.api.dto.WeatherMessageResponse;
import org.sopt.pawkey.backendapi.domain.weather.application.service.WeatherService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RouteStartFacade {
	private final UserService userService;
	private final WeatherService weatherService;

	@Transactional
	public WeatherMessageResponse getReadyData(Long userId) {
		UserEntity user = userService.findById(userId);
		return weatherService.getWeatherMessage(user.getRegion());
	}
}