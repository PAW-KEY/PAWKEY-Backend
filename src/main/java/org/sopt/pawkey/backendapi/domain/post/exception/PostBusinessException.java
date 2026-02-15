package org.sopt.pawkey.backendapi.domain.post.exception;

import org.sopt.pawkey.backendapi.global.exception.BusinessException;

public class PostBusinessException extends BusinessException {
	public PostBusinessException(final PostErrorCode errorCode) {
		super(errorCode);
	}
}