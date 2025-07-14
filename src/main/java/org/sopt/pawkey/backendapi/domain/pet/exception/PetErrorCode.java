package org.sopt.pawkey.backendapi.domain.pet.exception;

import org.sopt.pawkey.backendapi.global.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public enum PetErrorCode implements ErrorCode {

	CATEGORY_OPTION_NOT_FOUND("P40401", "존재하지 않는 성향 옵션입니다.", HttpStatus.NOT_FOUND);

	private final String code;
	private final String message;
	private final HttpStatus status;

	/**
	 * Constructs a PetErrorCode enum constant with the specified error code, message, and HTTP status.
	 *
	 * @param code    the unique error code identifier
	 * @param message the descriptive error message
	 * @param status  the associated HTTP status for this error
	 */
	PetErrorCode(final String code, final String message, final HttpStatus status) {
		this.code = code;
		this.message = message;
		this.status = status;
	}

	/**
	 * Returns the unique error code associated with this pet-related error.
	 *
	 * @return the error code string
	 */
	@Override
	public String getCode() {
		return code;
	}

	/**
	 * Returns the error message associated with this pet error code.
	 *
	 * @return the descriptive error message
	 */
	@Override
	public String getMessage() {
		return message;
	}

	/**
	 * Returns the HTTP status associated with this pet error code.
	 *
	 * @return the corresponding {@link HttpStatus}
	 */
	@Override
	public HttpStatus getStatus() {
		return status;
	}
}
