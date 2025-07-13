package org.sopt.pawkey.backendapi.domain.routes.exception;

import org.sopt.pawkey.backendapi.global.exception.ErrorCode;
import org.springframework.http.HttpStatus;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public enum RouteErrorCode implements ErrorCode {

	ROUTE_NOT_FOUND(HttpStatus.NOT_FOUND, "R40401", "해당 경로를 찾을 수 없습니다."),
	USER_PET_NOT_REGISTERED(HttpStatus.NOT_FOUND, "R40402", "유저에게 등록된 반려견이 존재하지 않습니다."),
	ROUTE_SHOW_FORBIDDEN(HttpStatus.FORBIDDEN, "R40301", "조회할 수 없는 경로입니다.");

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
