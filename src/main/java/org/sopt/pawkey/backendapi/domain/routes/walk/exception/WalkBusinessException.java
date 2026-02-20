package org.sopt.pawkey.backendapi.domain.routes.walk.exception;

import org.sopt.pawkey.backendapi.global.exception.BusinessException;
import org.sopt.pawkey.backendapi.global.exception.ErrorCode;

public class WalkBusinessException extends BusinessException {
    public WalkBusinessException(ErrorCode errorCode) {
        super(errorCode);
    }
}
