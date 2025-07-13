package org.sopt.pawkey.backendapi.global.infra.s3;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.sopt.pawkey.backendapi.domain.common.ImageStorage;
import org.sopt.pawkey.backendapi.global.exception.S3BusinessException;
import org.sopt.pawkey.backendapi.global.exception.S3ErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class S3ImageStorage implements ImageStorage {

	private final AmazonS3 amazonS3;
	@Value("${cloud.aws.s3.bucket}")
	private String bucketName;

	@Override
	public String uploadImage(MultipartFile image, String dir) {
		return uploadImageByFolder(image, dir);
	}

	@Override
	public List<String> uploadImages(List<MultipartFile> images, String dir) {
		if (images.size() > 3) {
			throw new S3BusinessException(S3ErrorCode.TOO_MANY_FILES);
		}

		List<String> uploadedUrls = new ArrayList<>();
		for (MultipartFile image : images) {
			uploadedUrls.add(uploadImageByFolder(image, dir));
		}
		return uploadedUrls;
	}

	@Override
	public void deleteImage(String address) {
		String key = getKeyFromImageAddress(address);
		try {
			amazonS3.deleteObject(new DeleteObjectRequest(bucketName, key));
		} catch (Exception e) {
			throw new S3BusinessException(S3ErrorCode.IO_EXCEPTION_ON_IMAGE_DELETE);
		}
	}

	private String uploadImageByFolder(MultipartFile image, String folder) {
		if (image.isEmpty() || Objects.isNull(image.getOriginalFilename())) {
			throw new S3BusinessException(S3ErrorCode.EMPTY_FILE_EXCEPTION);
		}

		validateImageFileExtension(image.getOriginalFilename());

		try {
			return uploadImageToS3(image, folder);
		} catch (IOException e) {
			throw new S3BusinessException(S3ErrorCode.IO_EXCEPTION_ON_IMAGE_UPLOAD);
		}
	}

	private String uploadImageToS3(MultipartFile image, String folder) throws IOException {
		String originalFilename = image.getOriginalFilename();
		String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
		String s3FileName = folder + "/" + UUID.randomUUID().toString().substring(0, 10) + originalFilename;

		InputStream is = image.getInputStream();
		byte[] bytes = IOUtils.toByteArray(is);

		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentType("image/" + extension.replace(".", ""));
		metadata.setContentLength(bytes.length);

		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);

		try {
			PutObjectRequest putObjectRequest =
				new PutObjectRequest(bucketName, s3FileName, byteArrayInputStream, metadata)
					.withCannedAcl(CannedAccessControlList.PublicRead);
			amazonS3.putObject(putObjectRequest);
		} catch (Exception e) {
			throw new S3BusinessException(S3ErrorCode.PUT_OBJECT_EXCEPTION);
		} finally {
			byteArrayInputStream.close();
			is.close();
		}

		return amazonS3.getUrl(bucketName, s3FileName).toString();
	}

	private void validateImageFileExtension(String filename) {
		int lastDotIndex = filename.lastIndexOf(".");
		if (lastDotIndex == -1) {
			throw new S3BusinessException(S3ErrorCode.NO_FILE_EXTENSION);
		}

		String extension = filename.substring(lastDotIndex + 1).toLowerCase();
		List<String> allowedExtensions = Arrays.asList("jpg", "jpeg", "png", "gif");

		if (!allowedExtensions.contains(extension)) {
			throw new S3BusinessException(S3ErrorCode.INVALID_FILE_EXTENSION);
		}
	}

	private String getKeyFromImageAddress(String imageAddress) {
		try {
			URL url = new URL(imageAddress);
			String decodingKey = URLDecoder.decode(url.getPath(), StandardCharsets.UTF_8);
			return decodingKey.substring(1);
		} catch (MalformedURLException e) {
			throw new S3BusinessException(S3ErrorCode.IO_EXCEPTION_ON_IMAGE_DELETE);
		}
	}
}
