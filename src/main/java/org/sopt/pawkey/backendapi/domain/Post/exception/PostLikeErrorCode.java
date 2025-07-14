package org.sopt.pawkey.backendapi.domain.post.exception;

import org.sopt.pawkey.backendapi.global.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public enum PostLikeErrorCode implements ErrorCode {

	DUPLICATE_LIKE("PL40901", "이미 좋아요를 누른 게시글입니다.", HttpStatus.CONFLICT),
	CANNOT_LIKE_OWN_POST("PL40001", "자신의 게시글에는 좋아요를 누르거나 취소할 수 없습니다.", HttpStatus.BAD_REQUEST),
	LIKE_NOT_FOUND("PL40401", "좋아요를 누른 이력이 없습니다.", HttpStatus.NOT_FOUND);

	private final String code;
	private final String message;
	private final HttpStatus status;

	PostLikeErrorCode(final String code, final String message, final HttpStatus status) {
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
