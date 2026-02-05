package org.sopt.pawkey.backendapi.domain.homeWeather.infra.persistence;

import java.util.Optional;

import org.sopt.pawkey.backendapi.domain.homeWeather.infra.persistence.entity.WeatherEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataWeatherRepository extends JpaRepository<WeatherEntity, Long> {
	Optional<WeatherEntity> findByRegionId(Long regionId);
}