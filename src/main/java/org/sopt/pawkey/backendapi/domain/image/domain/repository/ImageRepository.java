package org.sopt.pawkey.backendapi.domain.image.domain.repository;

import java.util.List;

import org.sopt.pawkey.backendapi.domain.image.infra.persistence.entity.ImageEntity;

public interface ImageRepository {
	ImageEntity save(ImageEntity imageEntity);

	List<ImageEntity> saveAll(List<ImageEntity> entities);
}
