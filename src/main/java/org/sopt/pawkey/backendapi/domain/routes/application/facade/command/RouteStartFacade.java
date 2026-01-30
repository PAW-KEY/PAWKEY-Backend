package org.sopt.pawkey.backendapi.domain.routes.application.facade.command;

import org.sopt.pawkey.backendapi.domain.user.application.service.UserService;
import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.UserEntity;
import org.sopt.pawkey.backendapi.domain.homeWeather.api.dto.WeatherMessageResponseDTO;
import org.sopt.pawkey.backendapi.domain.homeWeather.application.dto.result.WeatherMessageResult;
import org.sopt.pawkey.backendapi.domain.homeWeather.application.service.WeatherService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RouteStartFacade {
	private final UserService userService;
	private final WeatherService weatherService;

	@Transactional
	public WeatherMessageResponseDTO getReadyData(Long userId) {
		UserEntity user = userService.findById(userId);
		WeatherMessageResult result = weatherService.getWeatherMessage(user.getRegion());
		return WeatherMessageResponseDTO.of(result.mainMessage(), result.subMessage());
	}
}