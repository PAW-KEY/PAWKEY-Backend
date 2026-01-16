package org.sopt.pawkey.backendapi.domain.weather.domain.model;

public record WeatherMessage(
	String mainMessage,
	String subMessage
) {
}