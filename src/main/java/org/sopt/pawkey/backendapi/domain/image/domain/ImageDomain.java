package org.sopt.pawkey.backendapi.domain.image.domain;

import org.sopt.pawkey.backendapi.domain.image.exception.ImageBusinessException;
import org.sopt.pawkey.backendapi.domain.image.exception.ImageErrorCode;
import org.sopt.pawkey.backendapi.global.exception.ErrorCode;

public enum ImageDomain { //S3 경로의 최상위 prefix로 사용({domain}/{uuid}.{ext})
	WALK("walk"),
	ROUTE("route"),
	PET_PROFILE("profile");

	private final String path;

	ImageDomain(String path) {
		this.path = path;
	}

	public String getPath() {
		return path;
	}

	public static ImageDomain from(String domainStr) {
		if (domainStr == null || domainStr.isBlank()) {
			throw new ImageBusinessException(ImageErrorCode.INVALID_IMAGE_DOMAIN);
		}

		try {
			return ImageDomain.valueOf(domainStr.toUpperCase());
		} catch (IllegalArgumentException e) {
			// PET_PROFILE, WALK 등이 아닌 값이 들어오면 정의된 400 에러 코드를 반환
			throw new ImageBusinessException(ImageErrorCode.INVALID_IMAGE_DOMAIN);
		}
	}

}
