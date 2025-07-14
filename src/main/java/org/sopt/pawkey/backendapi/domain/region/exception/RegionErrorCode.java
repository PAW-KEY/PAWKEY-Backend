package org.sopt.pawkey.backendapi.domain.region.exception;

import org.sopt.pawkey.backendapi.global.exception.ErrorCode;
import org.springframework.http.HttpStatus;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public enum RegionErrorCode implements ErrorCode {

	REGION_NOT_FOUND(HttpStatus.NOT_FOUND, "R40401", "해당 지역을 찾을 수 없습니다."),
	REGION_TYPE_NOT_DONG(HttpStatus.BAD_REQUEST, "R40001", "소속 지역은 동 단위만 선택 가능합니다.");

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
