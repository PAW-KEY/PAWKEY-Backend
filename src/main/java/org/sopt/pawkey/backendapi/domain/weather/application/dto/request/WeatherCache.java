package org.sopt.pawkey.backendapi.domain.weather.application.dto.request;

import java.io.Serializable;

import org.sopt.pawkey.backendapi.domain.weather.infra.persistence.entity.WeatherEntity;

import lombok.Builder;

@Builder
public record WeatherCache(
	Integer temperature,
	Integer rainyMm,
	Integer rainyProb,
	Integer weatherCode
) implements Serializable {

	/**
	 * WeatherEntity를 WeatherCache DTO로 변환합니다. (저장 시 사용)
	 */
	public static WeatherCache from(WeatherEntity entity) {
		return WeatherCache.builder()
			.temperature(entity.getTemperature())
			.rainyMm(entity.getRainyMm())
			.rainyProb(entity.getRainyProb())
			.weatherCode(entity.getWeatherCode())
			.build();
	}

	/**
	 * WeatherCache 데이터를 바탕으로 WeatherEntity 객체를 생성합니다. (조회 시 사용)
	 * 서비스 계층에서 빌더를 직접 사용하는 복잡함을 줄여줍니다.
	 */
	public WeatherEntity toEntity(Long regionId) {
		return WeatherEntity.builder()
			.regionId(regionId)
			.temperature(this.temperature)
			.rainyMm(this.rainyMm)
			.rainyProb(this.rainyProb)
			.weatherCode(this.weatherCode)
			.build();
	}
}
