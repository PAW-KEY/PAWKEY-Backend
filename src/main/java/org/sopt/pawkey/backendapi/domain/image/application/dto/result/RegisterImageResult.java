package org.sopt.pawkey.backendapi.domain.image.application.dto.result;

import org.sopt.pawkey.backendapi.domain.image.infra.persistence.entity.ImageEntity;

public record RegisterImageResult(Long imageId) {
	public static RegisterImageResult from(ImageEntity image) {
		return new RegisterImageResult(image.getImageId());
	}
}
