package org.sopt.pawkey.backendapi.global.auth.exception;

import org.sopt.pawkey.backendapi.global.exception.ErrorCode;
import org.springframework.http.HttpStatus;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public enum AuthErrorCode implements ErrorCode {

	// JWT/Token 관련 오류
	ACCESS_TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "A40101", "유효하지 않은 Access Token입니다."),
	REFRESH_TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "A40102", "유효하지 않거나 만료된 Refresh Token입니다."),
	TOKEN_SIGNATURE_INVALID(HttpStatus.UNAUTHORIZED, "A40103", "토큰 서명이 유효하지 않습니다."),
	TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "A40104", "토큰이 만료되었습니다."),
	TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "A40105", "인증 토큰이 누락되었습니다."),
	TOKEN_DEVICE_ID_INVALID(HttpStatus.UNAUTHORIZED, "A40107", "요청된 기기 ID와 토큰 내부의 기기 ID가 일치하지 않습니다."),

	// 소셜 로그인 관련 오류
	SOCIAL_LOGIN_FAIL(HttpStatus.UNAUTHORIZED, "A40106", "소셜 로그인 인증에 실패했습니다. (외부 서버 오류 등)"),
	SOCIAL_ID_INVALID(HttpStatus.BAD_REQUEST, "A40001", "소셜 프로바이더로부터 유효한 ID를 얻지 못했습니다."),
	SOCIAL_WITHDRAW_FAIL(HttpStatus.BAD_REQUEST, "A40001", "소셜 로그인 탈퇴 실패");
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
