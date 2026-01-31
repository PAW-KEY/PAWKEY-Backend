package org.sopt.pawkey.backendapi.domain.image.application.service.command;

import static org.sopt.pawkey.backendapi.domain.image.exception.ImageErrorCode.*;

import org.sopt.pawkey.backendapi.domain.image.domain.repository.ImageRepository;
import org.sopt.pawkey.backendapi.domain.image.exception.ImageBusinessException;
import org.sopt.pawkey.backendapi.domain.image.infra.persistence.entity.ImageEntity;

import org.sopt.pawkey.backendapi.domain.image.domain.ImageDomain;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ImageService {

	private final ImageRepository imageRepository;

	public ImageEntity getImageById(Long imageId) {
		return imageRepository.findById(imageId)
			.orElseThrow(() -> new ImageBusinessException(IMAGE_NOT_FOUND));
	}

	public void deleteImageById(Long imageId) {
		ImageEntity image = getImageById(imageId);
		imageRepository.delete(image);
	}


	public ImageEntity createUploadedImage(String imageUrl, String contentType, int width, int height, ImageDomain domain) {
		ImageEntity image = ImageEntity.builder()
			.imageUrl(imageUrl)
			.extension(contentType)
			.width(width)
			.height(height)
			.domain(domain)
			.build();
		return imageRepository.save(image);
	}
}

