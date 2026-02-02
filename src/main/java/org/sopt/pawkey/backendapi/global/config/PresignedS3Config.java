package org.sopt.pawkey.backendapi.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Configuration
public class PresignedS3Config {

	@Value("${cloud.presign.region}")
	private String region;

	@Bean
	public S3Presigner s3Presigner() {
		return S3Presigner.builder()
			.region(Region.of(region))
			.credentialsProvider(DefaultCredentialsProvider.create())
			.build();
	}
}