package org.sopt.pawkey.backendapi.domain.auth.infra.persistence.entity;

import java.time.LocalDateTime;

import org.sopt.pawkey.backendapi.domain.auth.infra.persistence.converter.AppleRefreshTokenEncryptor;
import org.springframework.data.annotation.CreatedDate;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "apple_refresh_token")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class AppleRefreshTokenEntity {
	@Id
	private Long userId;

	@Convert(converter = AppleRefreshTokenEncryptor.class)
	@Column(nullable = false, length = 2048)
	private String refreshToken;

	@CreatedDate
	private LocalDateTime createdAt;
}
