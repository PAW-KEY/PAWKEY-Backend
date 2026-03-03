package org.sopt.pawkey.backendapi.domain.routes.exception;

import org.sopt.pawkey.backendapi.global.exception.ErrorCode;
import org.springframework.http.HttpStatus;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public enum RouteErrorCode implements ErrorCode {

	ROUTE_NOT_FOUND(HttpStatus.NOT_FOUND, "R40401", "해당 경로를 찾을 수 없습니다."),
	INVALID_ROUTE_COORDINATES(HttpStatus.BAD_REQUEST, "R40001", "유효하지 않은 좌표 데이터입니다."),
	ROUTE_SHOW_FORBIDDEN(HttpStatus.FORBIDDEN, "R40301", "조회할 수 없는 경로입니다."),
	RECO_STAT_NOT_FOUND(HttpStatus.NOT_FOUND, "R40402", "추천 통계 데이터를 찾을 수 없습니다."),
	RECO_TYPE_INVALID(HttpStatus.BAD_REQUEST, "R40002", "유효하지 않은 추천 요청 타입입니다."),
	REDIS_CONNECTION_FAILURE(HttpStatus.SERVICE_UNAVAILABLE, "R50301", "캐시 서비스 연결에 실패했습니다.");

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
