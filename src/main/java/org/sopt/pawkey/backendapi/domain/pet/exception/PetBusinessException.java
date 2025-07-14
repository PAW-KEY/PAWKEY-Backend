package org.sopt.pawkey.backendapi.domain.pet.exception;

import org.sopt.pawkey.backendapi.global.exception.BusinessException;

public class PetBusinessException extends BusinessException {
	/**
	 * Constructs a new PetBusinessException with the specified PetErrorCode.
	 *
	 * @param errorCode the error code representing the specific pet-related business error
	 */
	public PetBusinessException(PetErrorCode errorCode) {
		super(errorCode);
	}
}
