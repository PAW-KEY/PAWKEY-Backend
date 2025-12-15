package org.sopt.pawkey.backendapi.domain.auth.exception;

import org.sopt.pawkey.backendapi.global.exception.BusinessException;
import org.sopt.pawkey.backendapi.global.exception.ErrorCode;

public class AuthBusinessException extends BusinessException {
	public AuthBusinessException(ErrorCode errorCode) {

		super(errorCode);

	}
}
