package org.sopt.pawkey.backendapi.domain.weather.domain.repository;

import java.util.Optional;

import org.sopt.pawkey.backendapi.domain.weather.infra.persistence.entity.WeatherEntity;

public interface WeatherRepository {
	Optional<WeatherEntity> findByRegionId(Long regionId);
	WeatherEntity save(WeatherEntity weather);
}
