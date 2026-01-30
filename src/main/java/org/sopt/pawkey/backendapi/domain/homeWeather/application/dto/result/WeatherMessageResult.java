package org.sopt.pawkey.backendapi.domain.homeWeather.application.dto.result;

public record WeatherMessageResult(
	String mainMessage,
	String subMessage
) {
	public static WeatherMessageResult defaultMessage() {
		return new WeatherMessageResult(
			"반려견과 함께 즐거운 산책 시간 되세요!",
			"현재 날씨 정보를 불러올 수 없지만, 산책하기 좋은 날이에요."
		);
	}
}