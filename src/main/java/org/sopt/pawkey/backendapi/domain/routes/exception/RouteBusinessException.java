package org.sopt.pawkey.backendapi.domain.routes.exception;

import org.sopt.pawkey.backendapi.global.exception.BusinessException;
import org.sopt.pawkey.backendapi.global.exception.ErrorCode;

public class RouteBusinessException extends BusinessException {
	public RouteBusinessException(ErrorCode errorCode) {
		super(errorCode);
	}
}

