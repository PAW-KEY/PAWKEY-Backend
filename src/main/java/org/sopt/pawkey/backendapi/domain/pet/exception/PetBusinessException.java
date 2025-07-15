package org.sopt.pawkey.backendapi.domain.pet.exception;

import org.sopt.pawkey.backendapi.global.exception.BusinessException;

public class PetBusinessException extends BusinessException {
	public PetBusinessException(PetErrorCode errorCode) {
		super(errorCode);
	}
}
