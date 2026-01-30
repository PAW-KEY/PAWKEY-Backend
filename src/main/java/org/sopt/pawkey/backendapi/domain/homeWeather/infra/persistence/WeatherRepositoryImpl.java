package org.sopt.pawkey.backendapi.domain.homeWeather.infra.persistence;

import java.util.Optional;

import org.sopt.pawkey.backendapi.domain.homeWeather.domain.repository.WeatherRepository;
import org.sopt.pawkey.backendapi.domain.homeWeather.infra.persistence.entity.WeatherEntity;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class WeatherRepositoryImpl implements WeatherRepository {

	private final SpringDataWeatherRepository springDataWeatherRepository;

	@Override
	public Optional<WeatherEntity> findByRegionId(Long regionId) {
		return springDataWeatherRepository.findByRegionId(regionId);
	}

	@Override
	public WeatherEntity save(WeatherEntity weather) {
		return springDataWeatherRepository.save(weather);
	}
}
