package org.sopt.pawkey.backendapi.domain.routes.exception;

import org.sopt.pawkey.backendapi.global.exception.ErrorCode;
import org.springframework.http.HttpStatus;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public enum RouteErrorCode implements ErrorCode {

	ROUTE_NOT_FOUND(HttpStatus.NOT_FOUND, "R40401", "해당 경로를 찾을 수 없습니다."),
	INVALID_ROUTE_COORDINATES(HttpStatus.BAD_REQUEST, "R40001", "유효하지 않은 좌표 데이터입니다.");

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
