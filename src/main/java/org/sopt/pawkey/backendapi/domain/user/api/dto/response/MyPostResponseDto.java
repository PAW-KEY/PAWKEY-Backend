package org.sopt.pawkey.backendapi.domain.user.api.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import org.sopt.pawkey.backendapi.domain.pet.infra.persistence.entity.PetEntity;
import org.sopt.pawkey.backendapi.domain.post.infra.persistence.entity.PostEntity;
import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.UserEntity;

public record MyPostResponseDto(
	Long postId,
	LocalDateTime createdAt,
	Boolean isPublic,
	String title,
	String representativeImageUrl,
	WriterDto writer,
	List<String> descriptionTags
) {

	public static MyPostResponseDto from(PostEntity post) {
		String repImgUrl = post.getPostImageEntityList().stream()
			.findFirst()
			.map(img -> img.getImage().getImageUrl())
			.orElse(null);

		UserEntity writerEntity = post.getUser();
		PetEntity pet = writerEntity.getPet();

		return new MyPostResponseDto(
			post.getPostId(),
			post.getCreatedAt(),
			post.isPublic(),
			post.getTitle(),
			repImgUrl,
			new WriterDto(
				writerEntity.getUserId(),
				pet != null ? pet.getName() : null,
				pet != null && pet.getProfileImage() != null ? pet.getProfileImage().getImageUrl() : null
			),
			post.getPostCategoryOptionTop3EntityList().stream()
				.map(opt -> opt.getCategoryOption().getOptionText())
				.toList()
		);
	}

	public record WriterDto(
		Long userId,
		String petName,
		String petProfileImageUrl
	) {
	}
}
