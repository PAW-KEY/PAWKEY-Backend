package org.sopt.pawkey.backendapi.domain.dbti.exception;

import org.sopt.pawkey.backendapi.global.exception.BusinessException;
import org.sopt.pawkey.backendapi.global.exception.ErrorCode;

public class DbtiBusinessException extends BusinessException {
	public DbtiBusinessException(ErrorCode errorCode) {
		super(errorCode);
	}
}
