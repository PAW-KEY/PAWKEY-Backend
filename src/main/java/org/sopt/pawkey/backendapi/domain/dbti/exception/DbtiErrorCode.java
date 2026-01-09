package org.sopt.pawkey.backendapi.domain.dbti.exception;

import org.sopt.pawkey.backendapi.global.exception.ErrorCode;
import org.springframework.http.HttpStatus;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum DbtiErrorCode implements ErrorCode {

	DBTI_NOT_FOUND(HttpStatus.NOT_FOUND, "D40401", "해당 DBTI 유형 정보를 찾을 수 없습니다.");

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
