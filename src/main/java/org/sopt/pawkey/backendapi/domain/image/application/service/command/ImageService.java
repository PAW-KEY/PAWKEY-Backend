package org.sopt.pawkey.backendapi.domain.image.application.service.command;

import java.util.List;

import org.sopt.pawkey.backendapi.domain.image.infra.persistence.entity.ImageEntity;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

	ImageEntity storePetProfileImage(MultipartFile file);

	ImageEntity storeRouteImage(MultipartFile file);

	List<ImageEntity> storeWalkPostImages(List<MultipartFile> files);

	void deleteImage(ImageEntity imageEntity);
}
