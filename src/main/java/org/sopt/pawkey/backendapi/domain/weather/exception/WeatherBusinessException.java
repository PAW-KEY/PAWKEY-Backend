package org.sopt.pawkey.backendapi.domain.weather.exception;

import org.sopt.pawkey.backendapi.global.exception.BusinessException;
import org.sopt.pawkey.backendapi.global.exception.ErrorCode;

public class WeatherBusinessException extends BusinessException {
	public WeatherBusinessException(ErrorCode errorCode) {
		super(errorCode);
	}
}

