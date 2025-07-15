package org.sopt.pawkey.backendapi.domain.category.exception;

import org.sopt.pawkey.backendapi.global.exception.BusinessException;
import org.sopt.pawkey.backendapi.global.exception.ErrorCode;

public class CategoryBusinessException extends BusinessException {
	public CategoryBusinessException(ErrorCode errorCode) {
		super(errorCode);
	}
}
