package org.sopt.pawkey.backendapi.domain.post.application.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface PostImageService {
	String saveRouteImage(MultipartFile image) throws IOException;

	List<String> savePostImages(List<MultipartFile> images);
}
