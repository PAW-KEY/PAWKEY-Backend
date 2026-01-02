package org.sopt.pawkey.backendapi.domain.user.application.facade.query;

import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

import org.sopt.pawkey.backendapi.domain.post.api.dto.response.PostCardResponseDto;
import org.sopt.pawkey.backendapi.domain.post.infra.persistence.entity.PostLikeEntity;
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

	public List<PostCardResponseDto> getLikedPosts(Long userId) {
		// 1. 사용자 조회
		UserEntity user = userQueryRepository.getUserByUserId(userId)
			.orElseThrow(() -> new UserBusinessException(UserErrorCode.USER_NOT_FOUND));

		// 2. 좋아요한 PostLikeEntity 목록 조회
		List<PostLikeEntity> likedPosts = userLikedPostQueryService.findLikedPostsByUserWithPostAndImages(userId)
			.stream()
			.sorted(Comparator.comparing(PostLikeEntity::getPostLikeId).reversed())
			// 3. 비공개 게시물 필터링
			.filter(postLike -> postLike.getPost().isPublic())
			.toList();

		// 4. DTO 변환
		return likedPosts.stream()
			.map(postLike -> {
				var post = postLike.getPost();

				String regionName = post.getRoute().getRegion().getParent().getRegionName() + " " +
					post.getRoute().getRegion().getRegionName();

				int durationMinutes = (int)(post.getRoute().getDuration() / 60);

				return new PostCardResponseDto(
					post.getPostId(),
					regionName,
					post.getTitle(),
					post.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")),
					durationMinutes,
					true,
					post.getRoute().getTrackingImage().getImageUrl()
				);
			})
			.toList();
	}
}

