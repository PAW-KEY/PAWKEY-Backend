package org.sopt.pawkey.backendapi.domain.weather.application.facade;

import org.sopt.pawkey.backendapi.domain.region.exception.RegionBusinessException;
import org.sopt.pawkey.backendapi.domain.region.exception.RegionErrorCode;
import org.sopt.pawkey.backendapi.domain.region.infra.persistence.entity.RegionEntity;
import org.sopt.pawkey.backendapi.domain.user.application.service.UserQueryService;
import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.UserEntity;
import org.sopt.pawkey.backendapi.domain.weather.api.dto.RegionWeatherResponse;
import org.sopt.pawkey.backendapi.domain.weather.application.dto.request.WeatherCache;
import org.sopt.pawkey.backendapi.domain.weather.application.service.WeatherService;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class WeatherFacade {

	private final WeatherService weatherService;
	private final UserQueryService userQueryService;

	public RegionWeatherResponse getWeatherByUserRegion(Long userId) {

		UserEntity user = userQueryService.getUser(userId);
		RegionEntity region = user.getRegion();
		if (region == null) {
			throw new RegionBusinessException(RegionErrorCode.REGION_NOT_FOUND);
		}
		WeatherCache weather = weatherService.getOrFetchWeather(region);
		return RegionWeatherResponse.of(
			weather.temperature(),
			weather.rainyMm(),
			region.getRegionName()
		);
	}
}