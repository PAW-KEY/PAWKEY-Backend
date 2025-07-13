package org.sopt.pawkey.backendapi.domain.image.application.service.command;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.sopt.pawkey.backendapi.domain.common.ImageStorage;
import org.sopt.pawkey.backendapi.domain.image.domain.repository.ImageRepository;
import org.sopt.pawkey.backendapi.domain.image.infra.persistence.entity.ImageEntity;
import org.sopt.pawkey.backendapi.global.exception.BusinessException;
import org.sopt.pawkey.backendapi.global.exception.GlobalErrorCode;
import org.sopt.pawkey.backendapi.global.util.ImageDimensionUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

	private static final String PROFILE_DIR = "profile";
	private static final String ROUTE_DIR = "route";
	private static final String WALK_DIR = "walk";
	private final ImageRepository imageRepository;
	private final ImageStorage imageStorage;

	@Override
	public ImageEntity storePetProfileImage(MultipartFile file) {
		String imageUrl = imageStorage.uploadImage(file, PROFILE_DIR);
		ImageEntity imageEntity = createImage(file, imageUrl);

		return imageRepository.save(imageEntity);
	}

	@Override
	public ImageEntity storeRouteImage(MultipartFile file) {
		String imageUrl = imageStorage.uploadImage(file, ROUTE_DIR);
		ImageEntity imageEntity = createImage(file, imageUrl);

		return imageRepository.save(imageEntity);

	}

	@Override
	public List<ImageEntity> storeWalkPostImages(List<MultipartFile> files) {
		List<String> imageUrls = imageStorage.uploadImages(files, WALK_DIR);

		List<ImageEntity> images = new ArrayList<>();
		for (int i = 0; i < files.size(); i++) {
			MultipartFile file = files.get(i);
			String url = imageUrls.get(i);

			// ImageEntity 생성
			images.add(createImage(file, url));
		}

		return imageRepository.saveAll(images);
	}

	@Override
	public void deleteImage(ImageEntity imageEntity) {
		imageStorage.deleteImage(imageEntity.getImageUrl());
		imageRepository.delete(imageEntity);
	}

	private ImageEntity createImage(MultipartFile file, String imageUrl) {
		List<Integer> imageDimensions = List.of();
		try {
			imageDimensions = ImageDimensionUtil.getImageDimensions(file);
		} catch (IOException e) {
			throw new BusinessException(GlobalErrorCode.INTERNAL_SERVER_ERROR);
		}

		return ImageEntity.builder()
			.imageUrl(imageUrl)
			.extension(file.getContentType())
			.width(imageDimensions.get(0))
			.height(imageDimensions.get(1))
			.build();
	}
}
