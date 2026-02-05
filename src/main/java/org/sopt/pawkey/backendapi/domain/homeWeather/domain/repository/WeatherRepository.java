package org.sopt.pawkey.backendapi.domain.homeWeather.domain.repository;

import java.util.Optional;

import org.sopt.pawkey.backendapi.domain.homeWeather.infra.persistence.entity.WeatherEntity;

public interface WeatherRepository {
	Optional<WeatherEntity> findByRegionId(Long regionId);
	WeatherEntity save(WeatherEntity weather);
}
