package org.sopt.pawkey.backendapi.domain.user.application.facade.query;

import java.util.List;

import org.sopt.pawkey.backendapi.domain.post.infra.persistence.entity.PostLikeEntity;
import org.sopt.pawkey.backendapi.domain.user.api.dto.response.LikedPostResponseDto;
import org.sopt.pawkey.backendapi.domain.user.application.service.UserLikedPostQueryService;
import org.sopt.pawkey.backendapi.domain.user.domain.repository.UserQueryRepository;
import org.sopt.pawkey.backendapi.domain.user.exception.UserBusinessException;
import org.sopt.pawkey.backendapi.domain.user.exception.UserErrorCode;
import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.UserEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserLikedPostQueryFacade {

	private final UserQueryRepository userQueryRepository;
	private final UserLikedPostQueryService userLikedPostQueryService;

	public List<LikedPostResponseDto> getLikedPosts(Long userId) {

		UserEntity user = userQueryRepository.getUserByUserId(userId)
			.orElseThrow(() -> new UserBusinessException(UserErrorCode.USER_NOT_FOUND));

		List<PostLikeEntity> likedPosts = userLikedPostQueryService.findLikedPostsByUserWithPostAndImages(userId);

		return likedPosts.stream()
			.map(postLike -> {
				var post = postLike.getPost();

				String imageurldummy = "imageurl";

				// String repImageUrl = post.getPostImageEntityList()
				// 	.stream()
				// 	.min(Comparator.comparing(PostImageEntity::getCreatedAt)) // 또는 다른 명확한 기준
				// 	.map(PostImageEntity::getImage)      // ImageEntity
				// 	.map(imageEntity -> imageEntity.getImageUrl())  // String URL
				// 	.map("imageurl")
				// 	.orElse(null);

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
					imageurldummy,
					//repImageUrl,
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
