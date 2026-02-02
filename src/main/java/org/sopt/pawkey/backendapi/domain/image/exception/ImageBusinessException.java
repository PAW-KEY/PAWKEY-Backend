package org.sopt.pawkey.backendapi.domain.image.exception;

import org.sopt.pawkey.backendapi.global.exception.BusinessException;
import org.sopt.pawkey.backendapi.global.exception.ErrorCode;

public class ImageBusinessException extends BusinessException {
	public ImageBusinessException(ErrorCode errorCode) {
		super(errorCode);
	}
}

