package org.sopt.pawkey.backendapi.domain.tempImage.application.service;

import java.time.Duration;
import java.util.UUID;

import org.sopt.pawkey.backendapi.domain.tempImage.application.dto.result.IssuePresignedUrlResult;
import org.sopt.pawkey.backendapi.domain.tempImage.domain.ImageDomain;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

@Service
@RequiredArgsConstructor
public class PresignedImageService {

	private final S3Presigner s3Presigner;

	@Value("${cloud.presign.prod-bucket}")
	private String bucket;

	@Value("${cloud.presign.expire-minutes}")
	private long expireMinutes;


	public IssuePresignedUrlResult createPresignedUrl(ImageDomain domain, String contentType){
		String key = generateKey(domain,contentType); //key(식별자) 생성

		//s3 put 요청
		PutObjectRequest putObjectRequest =
			PutObjectRequest.builder()
							.bucket(bucket)
							.key(key)
							.contentType(contentType)
							.build();

		//PUT요청(putObjectRequest)에 대해, 유효시간 적용된 presignedurl 생성
		PresignedPutObjectRequest presignedRequest =
			s3Presigner.presignPutObject(
				PutObjectPresignRequest.builder()
					.putObjectRequest(putObjectRequest)
					.signatureDuration(Duration.ofMinutes(expireMinutes))
					.build()
			);

		// 발급된 presignedURL 클라이언트에게 전달
		return new IssuePresignedUrlResult(presignedRequest.url().toString(),buildImageUrl(bucket, key)
		);
	}

	//S3 key생성(이미지의 세부 domain기반으로 고유 식별자 형태로 저장)
	private String generateKey(ImageDomain domain,String contentType){
		String extension = contentType.substring(contentType.lastIndexOf("/") + 1);
		return domain.getPath() + "/" + UUID.randomUUID() + "." + extension;
	}

	private String buildImageUrl(String bucket, String key) { //S3 기본 공개 URL(나중에 이미지 조회 개선 CDN 도입 시, 메서드 수정 필요)
		return "https://" + bucket + ".s3.ap-northeast-2.amazonaws.com/" + key;
	}

}
