package org.sopt.pawkey.backendapi.domain.weather.api.dto;

import lombok.Builder;

@Builder
public record WeatherMessageResponse(
	String mainWriting,
	String subWriting
) {
	public static WeatherMessageResponse of(String main, String sub) {
		return WeatherMessageResponse.builder()
			.mainWriting(main)
			.subWriting(sub)
			.build();
	}
}