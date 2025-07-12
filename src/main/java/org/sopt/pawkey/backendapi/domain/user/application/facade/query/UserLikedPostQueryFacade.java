package org.sopt.pawkey.backendapi.domain.user.application.facade.query;

import java.util.List;

import org.sopt.pawkey.backendapi.domain.post.infra.persistence.entity.PostImageEntity;
import org.sopt.pawkey.backendapi.domain.post.infra.persistence.entity.PostLikeEntity;
import org.sopt.pawkey.backendapi.domain.user.api.dto.response.LikedPostResponseDto;
import org.sopt.pawkey.backendapi.domain.user.application.service.UserLikedPostQueryService;
import org.sopt.pawkey.backendapi.domain.user.domain.repository.UserQueryRepository;
import org.sopt.pawkey.backendapi.domain.user.exception.UserBusinessException;
import org.sopt.pawkey.backendapi.domain.user.exception.UserErrorCode;
import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.UserEntity;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserLikedPostQueryFacade {

	private final UserQueryRepository userQueryRepository;
	private final UserLikedPostQueryService userLikedPostQueryService;

	public List<LikedPostResponseDto> getLikedPosts(Long userId) {
		UserEntity user = userQueryRepository.getUserByUserId(userId)
			.orElseThrow(() -> new UserBusinessException(UserErrorCode.USER_NOT_FOUND));

		// fetch join 메서드 사용
		List<PostLikeEntity> likedPosts = userLikedPostQueryService.findLikedPostsByUserWithPostAndImages(user);

		return likedPosts.stream()
			.map(postLike -> {
				var post = postLike.getPost();

				String repImageUrl = post.getPostImageEntityList()
					.stream()
					.findFirst()
					.map(PostImageEntity::getImage)      // ImageEntity
					.map(imageEntity -> imageEntity.getImageUrl())  // String URL
					.orElse(null);

				var writer = post.getUser();
				Long writerId = writer.getUserId();
				String writerPetName = writer.getPet() != null ? writer.getPet().getName() : null;
				String writerProfileImgUrl = (writer.getPet() != null && writer.getPet().getProfileImage() != null) ?
					writer.getPet().getProfileImage().getImageUrl() : null;

				List<String> descriptionTags = post.getPostCategoryOptionTop3EntityList()
					.stream()
					.map(optionTop3 -> optionTop3.getCategoryOption().getOptionText())
					.toList();

				boolean isLiked = true;

				return new LikedPostResponseDto(
					post.getPostId(),
					post.getCreatedAt(),
					isLiked,
					repImageUrl,
					new LikedPostResponseDto.WriterDto(
						writerId,
						writerPetName,
						writerProfileImgUrl
					),
					descriptionTags
				);
			})
			.toList();
	}
}
