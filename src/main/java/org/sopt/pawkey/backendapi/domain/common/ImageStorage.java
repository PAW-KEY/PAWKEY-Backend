package org.sopt.pawkey.backendapi.domain.common;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface ImageStorage {

	String uploadImage(MultipartFile image, String dir);

	List<String> uploadImages(List<MultipartFile> images, String dir);

	void deleteImage(String address);
}
