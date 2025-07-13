package org.sopt.pawkey.backendapi.domain.image.infra.persistence;

import java.util.List;

import org.sopt.pawkey.backendapi.domain.image.domain.repository.ImageRepository;
import org.sopt.pawkey.backendapi.domain.image.infra.persistence.entity.ImageEntity;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ImageRepositoryImpl implements ImageRepository {

	private final SpringDataImageRepository imageRepository;

	public ImageEntity save(ImageEntity entity) {
		return imageRepository.save(entity);
	}

	public List<ImageEntity> saveAll(List<ImageEntity> entities) {
		return imageRepository.saveAll(entities);
	}

	@Override
	public void delete(ImageEntity imageEntity) {
		imageRepository.delete(imageEntity);
	}
}
