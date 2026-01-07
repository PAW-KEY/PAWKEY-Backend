package org.sopt.pawkey.backendapi.domain.weather.exception;

import org.sopt.pawkey.backendapi.global.exception.ErrorCode;
import org.springframework.http.HttpStatus;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public enum WeatherErrorCode implements ErrorCode {

	WEATHER_MESSAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "W001", "해당 날씨 조건에 맞는 멘트가 존재하지 않습니다."),
	WEATHER_FETCH_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "W002", "날씨 정보를 가져오는 데 실패했습니다.");
	private final HttpStatus status;
	private final String code;
	private final String message;

	@Override
	public String getCode() {
		return code;
	}

	@Override
	public String getMessage() {
		return message;
	}

	@Override
	public HttpStatus getStatus() {
		return status;
	}
}
