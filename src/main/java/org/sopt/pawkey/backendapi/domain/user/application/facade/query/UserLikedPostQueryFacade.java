package org.sopt.pawkey.backendapi.domain.user.application.facade.query;

import java.util.Comparator;
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
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserLikedPostQueryFacade {

	private final UserQueryRepository userQueryRepository;
	private final UserLikedPostQueryService userLikedPostQueryService;

	public List<LikedPostResponseDto> getLikedPosts(Long userId) {
		// 1. 사용자 조회
		UserEntity user = userQueryRepository.getUserByUserId(userId)
			.orElseThrow(() -> new UserBusinessException(UserErrorCode.USER_NOT_FOUND));

		// 2. 좋아요한 PostLikeEntity 목록 조회
		List<PostLikeEntity> likedPosts = userLikedPostQueryService.findLikedPostsByUserWithPostAndImages(userId);

		// 3. 최신순 정렬
		likedPosts.sort(Comparator.comparing((PostLikeEntity pl) -> pl.getPost().getCreatedAt()).reversed());

		// 4. DTO 변환
		return likedPosts.stream()
			.map(postLike -> {
				var post = postLike.getPost();

				// 대표 이미지: 가장 먼저 등록된 이미지
				String repImageUrl = post.getPostImageEntityList().stream()
					.min(Comparator.comparing(PostImageEntity::getCreatedAt))
					.map(PostImageEntity::getImage)
					.map(imageEntity -> imageEntity.getImageUrl())
					.orElse(null);

				// 작성자 정보
				var writer = post.getUser();
				var pet = writer.getPetEntityList().stream()
					.findFirst()
					.orElse(null);
				Long writerId = writer.getUserId();
				String writerPetName = pet != null ? pet.getName() : null;
				String writerProfileImgUrl = (pet != null && pet.getProfileImage() != null)
					? pet.getProfileImage().getImageUrl()
					: null;

				// 설명 태그
				List<String> descriptionTags = post.getPostCategoryOptionTop3EntityList()
					.stream()
					.map(optionTop3 -> optionTop3.getCategoryOption().getOptionText())
					.toList();

				// DTO 반환
				return new LikedPostResponseDto(
					post.getPostId(),
					post.getCreatedAt(),
					true,
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

