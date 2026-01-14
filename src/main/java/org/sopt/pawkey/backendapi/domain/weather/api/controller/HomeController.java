package org.sopt.pawkey.backendapi.domain.weather.api.controller;

import static org.sopt.pawkey.backendapi.global.constants.AppConstants.*;

import org.sopt.pawkey.backendapi.domain.auth.annotation.UserId;
import org.sopt.pawkey.backendapi.domain.weather.api.dto.RegionWeatherResponse;
import org.sopt.pawkey.backendapi.domain.weather.application.facade.WeatherFacade;
import org.sopt.pawkey.backendapi.global.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(API_PREFIX + "/home")
public class HomeController {

	private final WeatherFacade weatherFacade;

	@Operation(summary = "[홈] 날씨 정보 조회", description = "로그인한 유저의 지역을 기반으로 날씨 정보를 제공합니다.", tags = {"Home"})
	@ApiResponses({
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "날씨 정보 조회 성공"),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "유저 또는 지역 정보를 찾을 수 없음"),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 내부 오류")
	})
	@GetMapping("/weather")
	public ResponseEntity<ApiResponse<RegionWeatherResponse>> getHomeWeather(
		@Parameter(hidden = true) @UserId Long userId
	) {
		RegionWeatherResponse response = weatherFacade.getWeatherByUserRegion(userId);
		return ResponseEntity.ok(ApiResponse.success(response));
	}
}
