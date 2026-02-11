package org.sopt.pawkey.backendapi.domain.post.exception;

import org.sopt.pawkey.backendapi.global.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public enum PostErrorCode implements ErrorCode {

	POST_NOT_FOUND("P40401", "존재하지 않는 게시글입니다.", HttpStatus.NOT_FOUND),
	POST_WRITE_FORBIDDEN("P40301", "본인의 경로에 대해서만 게시글 작성이 가능합니다.", HttpStatus.FORBIDDEN),
	ALREADY_ROUTE_POST_EXIST("P40901", "이미 경로에 대한 게시물이 존재합니다.", HttpStatus.CONFLICT),
	INVALID_FILTER_OPTION("P40001", "유효하지 않은 필터 옵션 값이 포함되어 있습니다.", HttpStatus.BAD_REQUEST),
	INVALID_CURSOR_FORMAT("P40002", "잘못된 커서 형식입니다.", HttpStatus.BAD_REQUEST);

	private final String code;
	private final String message;
	private final HttpStatus status;

	PostErrorCode(final String code, final String message, final HttpStatus status) {
		this.code = code;
		this.message = message;
		this.status = status;
	}

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
