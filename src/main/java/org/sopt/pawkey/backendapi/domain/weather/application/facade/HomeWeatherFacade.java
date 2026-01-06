package org.sopt.pawkey.backendapi.domain.weather.application.facade;

import org.sopt.pawkey.backendapi.domain.region.infra.persistence.entity.RegionEntity;
import org.sopt.pawkey.backendapi.domain.user.application.service.UserQueryService;
import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.UserEntity;
import org.sopt.pawkey.backendapi.domain.weather.api.dto.HomeWeatherResponse;
import org.sopt.pawkey.backendapi.domain.weather.application.service.WeatherService;
import org.sopt.pawkey.backendapi.domain.weather.infra.persistence.entity.WeatherEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class HomeWeatherFacade {

	private final WeatherService weatherService;
	private final UserQueryService userQueryService;

	@Transactional
	public HomeWeatherResponse getHomeWeather(Long userId) {

		UserEntity user = userQueryService.getUser(userId);
		RegionEntity region = user.getRegion();
		WeatherEntity weather = weatherService.getOrFetchWeather(region);

		return HomeWeatherResponse.of(
			weather.getTemperature(),
			weather.getRainyMm(),
			region.getRegionName()
		);
	}
}