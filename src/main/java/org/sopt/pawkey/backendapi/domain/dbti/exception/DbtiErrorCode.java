package org.sopt.pawkey.backendapi.domain.dbti.exception;

import org.sopt.pawkey.backendapi.global.exception.ErrorCode;
import org.springframework.http.HttpStatus;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum DbtiErrorCode implements ErrorCode {

	INVALID_OPTION_COUNT(HttpStatus.BAD_REQUEST, "D40001", "답변 개수가 올바르지 않습니다. (9개의 답변 필요)"),
	INVALID_OPTION_IDS(HttpStatus.BAD_REQUEST, "D40002", "유효하지 않은 선택지 ID가 포함되어 있습니다."),
	DBTI_NOT_FOUND(HttpStatus.NOT_FOUND, "D40401", "해당 DBTI 유형 정보를 찾을 수 없습니다."),
	DUPLICATE_DBTI_RESULT(HttpStatus.CONFLICT, "D40901", "이미 DBTI 검사 결과가 존재하는 반려견입니다.");

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
