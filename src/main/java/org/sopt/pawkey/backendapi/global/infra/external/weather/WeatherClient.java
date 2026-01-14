package org.sopt.pawkey.backendapi.global.infra.external.weather;

import org.sopt.pawkey.backendapi.global.infra.external.weather.dto.OpenWeatherResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "WeatherClient", url = "${weather.url}")
public interface WeatherClient {

	@GetMapping("/weather")
	OpenWeatherResponse getCurrentWeather(
		@RequestParam("lat") double lat,
		@RequestParam("lon") double lon,
		@RequestParam("appid") String apiKey,
		@RequestParam("units") String units // metric 사용 (화씨를 섭씨로 변환 가능)
	);
}