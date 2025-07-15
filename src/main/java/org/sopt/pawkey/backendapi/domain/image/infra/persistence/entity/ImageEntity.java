package org.sopt.pawkey.backendapi.domain.image.infra.persistence.entity;

import org.sopt.pawkey.backendapi.global.infra.persistence.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

	@Builder
	public ImageEntity(
		Long imageId,
		String imageUrl,
		String extension,
		int width,
		int height
	) {
		this.imageId = imageId;
		this.imageUrl = imageUrl;
		this.extension = extension;
		this.width = width;
		this.height = height;
	}

}
