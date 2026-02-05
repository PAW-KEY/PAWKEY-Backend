package org.sopt.pawkey.backendapi.domain.homeWeather.exception;

import org.sopt.pawkey.backendapi.global.exception.ErrorCode;
import org.springframework.http.HttpStatus;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public enum WeatherErrorCode implements ErrorCode {

	WEATHER_MESSAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "W001", "해당 날씨 조건에 맞는 멘트가 존재하지 않습니다."),
	WEATHER_NOT_FOUND(HttpStatus.NOT_FOUND, "W001", "해당 날씨에 대한 정보가 존재하지 않습니다."),
	WEATHER_FETCH_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "W002", "날씨 정보를 가져오는 데 실패했습니다."),
	WEATHER_DATA_INCOMPLETE(HttpStatus.INTERNAL_SERVER_ERROR, "W003", "날씨 정보가 불완전하여 메시지를 생성할 수 없습니다.");
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
