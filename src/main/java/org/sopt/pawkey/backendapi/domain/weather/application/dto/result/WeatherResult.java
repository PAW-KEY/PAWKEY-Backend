package org.sopt.pawkey.backendapi.domain.weather.application.dto.result;

import java.io.Serializable;

import org.sopt.pawkey.backendapi.domain.weather.infra.persistence.entity.WeatherEntity;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import lombok.Builder;

@Builder
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public record WeatherResult(
	Integer temperature,
	Integer rainyMm,
	Integer rainyProb,
	Integer weatherCode
) implements Serializable {

	/**
	 * WeatherEntity를 WeatherResult DTO로 변환합니다. (저장 시 사용)
	 */
	public static WeatherResult from(WeatherEntity entity) {
		return WeatherResult.builder()
			.temperature(entity.getTemperature())
			.rainyMm(entity.getRainyMm())
			.rainyProb(entity.getRainyProb())
			.weatherCode(entity.getWeatherCode())
			.build();
	}
}
