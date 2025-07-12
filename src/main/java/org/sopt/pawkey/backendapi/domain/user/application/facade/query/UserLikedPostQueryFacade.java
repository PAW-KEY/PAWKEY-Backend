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
		// 1) Optional 안전 처리
		UserEntity user = userQueryRepository.getUserByUserId(userId)
			.orElseThrow(() -> new UserBusinessException(UserErrorCode.USER_NOT_FOUND));

		// 2) 사용자가 좋아요한 PostLikeEntity 목록
		List<PostLikeEntity> likedPosts = userLikedPostQueryService.findLikedPostsByUser(user);

		// 3) 각 PostLikeEntity → LikedPostResponseDto 변환
		return likedPosts.stream()
			.map(postLike -> {
				var post = postLike.getPost();

				String repImageUrl = String.valueOf(post.getPostImageEntityList()
					.stream()
					.findFirst()                                         // 첫 번째 이미지
					.map(PostImageEntity::getImage));

				/* 작성자(UserEntity) 정보 */
				var writer = post.getUser();
				Long writerId              = writer.getUserId();
				String writerPetName       = writer.getPet().getName();
				String writerProfileImgUrl = writer.getPet().getProfileImage().getImageUrl();

				/* 설명 태그 → postCategoryOptionTop3EntityList의 getOptionText 문자열로 */
				List<String> descriptionTags = post.getPostCategoryOptionTop3EntityList()
					.stream()
					.map(optionTop3 -> optionTop3.getCategoryOption().getOptionText())
					.toList();

				/* 일단 true.. */
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
