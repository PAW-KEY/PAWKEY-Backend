package org.sopt.pawkey.backendapi.domain.homeWeather.api.dto;

import lombok.Builder;

@Builder
public record WeatherMessageResponseDTO(
	String mainMessage,
	String subMessage
) {
	public static WeatherMessageResponseDTO of(String main, String sub) {
		return WeatherMessageResponseDTO.builder()
			.mainMessage(main)
			.subMessage(sub)
			.build();
	}
}