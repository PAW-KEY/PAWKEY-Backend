package org.sopt.pawkey.backendapi.global.infra.external.weather.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public record WeatherResponse(
	Main main,
	List<Weather> weather,
	@JsonProperty("dt") long timestamp,
	Rain rain,
	@JsonProperty("pop") Double pop // 강수 확률 (0~1 사이 값)
) {
	public record Main(double temp) {} // 기온 (섭씨)
	public record Weather(int id) {} // 날씨 상태 코드 (800: 맑음 등)
	public record Rain(@JsonProperty("1h") Double amount) {} // 1시간당 강수량
}