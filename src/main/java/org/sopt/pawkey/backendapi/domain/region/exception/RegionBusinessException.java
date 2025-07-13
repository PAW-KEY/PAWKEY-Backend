package org.sopt.pawkey.backendapi.domain.region.exception;

import org.sopt.pawkey.backendapi.global.exception.BusinessException;
import org.sopt.pawkey.backendapi.global.exception.ErrorCode;

public class RegionBusinessException extends BusinessException {
	public RegionBusinessException(ErrorCode errorCode) {
		super(errorCode);
	}
}


