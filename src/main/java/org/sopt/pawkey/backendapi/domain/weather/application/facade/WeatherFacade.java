package org.sopt.pawkey.backendapi.domain.weather.application.facade;

import org.sopt.pawkey.backendapi.domain.region.exception.RegionBusinessException;
import org.sopt.pawkey.backendapi.domain.region.exception.RegionErrorCode;
import org.sopt.pawkey.backendapi.domain.region.infra.persistence.entity.RegionEntity;
import org.sopt.pawkey.backendapi.domain.user.application.service.UserQueryService;
import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.UserEntity;
import org.sopt.pawkey.backendapi.domain.weather.api.dto.RegionWeatherResponseDTO;
import org.sopt.pawkey.backendapi.domain.weather.application.dto.request.WeatherResult;
import org.sopt.pawkey.backendapi.domain.weather.application.service.WeatherService;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class WeatherFacade {

	private final WeatherService weatherService;
	private final UserQueryService userQueryService;

	public RegionWeatherResponseDTO getWeatherByUserRegion(Long userId) {

		UserEntity user = userQueryService.getUser(userId);
		RegionEntity region = user.getRegion();
		if (region == null) {
			throw new RegionBusinessException(RegionErrorCode.REGION_NOT_FOUND);
		}
		WeatherResult weather = weatherService.getOrFetchWeather(region);
		return RegionWeatherResponseDTO.of(
			weather.temperature(),
			weather.rainyMm(),
			region.getRegionName()
		);
	}
}