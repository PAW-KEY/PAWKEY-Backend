package org.sopt.pawkey.backendapi.domain.pet.exception;

import org.sopt.pawkey.backendapi.global.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public enum PetErrorCode implements ErrorCode {

	CATEGORY_OPTION_NOT_FOUND("P40401", "존재하지 않는 성향 옵션입니다.", HttpStatus.NOT_FOUND),
	PET_NOT_FOUND("P40402", "존재하지 않는 반려견 정보입니다.", HttpStatus.NOT_FOUND);
	private final String code;
	private final String message;
	private final HttpStatus status;

	PetErrorCode(final String code, final String message, final HttpStatus status) {
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
