package org.sopt.pawkey.backendapi.domain.image.infra.persistence.entity;

import org.sopt.pawkey.backendapi.domain.image.exception.ImageBusinessException;
import org.sopt.pawkey.backendapi.domain.image.exception.ImageErrorCode;
import org.sopt.pawkey.backendapi.domain.routes.exception.RouteBusinessException;
import org.sopt.pawkey.backendapi.domain.routes.exception.RouteErrorCode;
import org.sopt.pawkey.backendapi.domain.tempImage.domain.ImageDomain;
import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.UserEntity;
import org.sopt.pawkey.backendapi.global.infra.persistence.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "images")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ImageEntity extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "image_id")
	private Long imageId;

	@Column(name = "image_url", nullable = false, length = 500)
	private String imageUrl;

	@Column(name = "extension", nullable = false)
	private String extension;

	@Column(name = "width", nullable = false)
	private int width;

	@Column(name = "height", nullable = false)
	private int height;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ImageDomain domain;


	@Builder
	public ImageEntity(
		Long imageId,
		String imageUrl,
		String extension,
		int width,
		int height,
		ImageDomain domain
	) {
		this.imageId = imageId;
		this.imageUrl = imageUrl;
		this.extension = extension;
		this.width = width;
		this.height = height;
		this.domain = domain;
	}

	public void validateUsableForRoute() {
		if (this.domain != ImageDomain.ROUTE) {
			throw new ImageBusinessException(ImageErrorCode.INVALID_IMAGE_DOMAIN);
		}
	}

}
