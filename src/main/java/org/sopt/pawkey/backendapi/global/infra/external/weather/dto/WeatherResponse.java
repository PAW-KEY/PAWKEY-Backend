package org.sopt.pawkey.backendapi.global.infra.external.weather.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public record WeatherResponse(
	Main main,
	List<Weather> weather,
	@JsonProperty("dt") long timestamp,
	Rain rain,
	@JsonProperty("pop") Double pop
) {
	public record Main(double temp) {
	}

	public record Weather(int id) {
	}

	public record Rain(@JsonProperty("1h") Double amount) {
	}

	public Integer getConvertedTemp() {
		if (main == null) {
			return null;
		}
		return (int)main.temp(); // 소수점 절사
	}

	public Integer getConvertedRain() {
		return (rain != null && rain.amount() != null) ? rain.amount().intValue() : 0;
	}

	public Integer getConvertedPop() {
		return (pop != null) ? (int)(pop * 100) : 0; // 0.1 -> 10%
	}

	public Integer getWeatherCode() {
		return (weather == null || weather.isEmpty()) ? 800 : weather.get(0).id();
	}
}