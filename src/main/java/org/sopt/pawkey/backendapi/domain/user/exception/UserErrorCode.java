package org.sopt.pawkey.backendapi.domain.user.exception;

import org.sopt.pawkey.backendapi.global.exception.ErrorCode;
import org.springframework.http.HttpStatus;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public enum UserErrorCode implements ErrorCode {

	USER_DUPLICATE_LOGIN_ID(HttpStatus.CONFLICT, "U40901", "중복된 로그인 아이디입니다."),
	USER_NOT_FOUND(HttpStatus.NOT_FOUND, "U40401", "유저를 찾을 수 없습니다."),
	USER_PET_NOT_REGISTERED(HttpStatus.NOT_FOUND, "U40402", "유저에게 등록된 반려견이 존재하지 않습니다.");

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
