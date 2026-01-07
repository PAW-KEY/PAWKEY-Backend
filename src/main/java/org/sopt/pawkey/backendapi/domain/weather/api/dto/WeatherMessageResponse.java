package org.sopt.pawkey.backendapi.domain.weather.api.dto;

import lombok.Builder;

@Builder
public record WeatherMessageResponse(
	String mainMessage,
	String subMessage
) {
	public static WeatherMessageResponse of(String main, String sub) {
		return WeatherMessageResponse.builder()
			.mainMessage(main)
			.subMessage(sub)
			.build();
	}
}