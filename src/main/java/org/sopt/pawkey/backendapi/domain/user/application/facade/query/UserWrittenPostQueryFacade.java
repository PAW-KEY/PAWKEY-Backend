package org.sopt.pawkey.backendapi.domain.user.application.facade.query;

import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

import org.sopt.pawkey.backendapi.domain.pet.infra.persistence.entity.PetEntity;
import org.sopt.pawkey.backendapi.domain.post.api.dto.response.PostCardResponseDto;
import org.sopt.pawkey.backendapi.domain.post.domain.repository.PostLikeRepository;
import org.sopt.pawkey.backendapi.domain.post.domain.repository.PostRepository;
import org.sopt.pawkey.backendapi.domain.post.infra.persistence.entity.PostEntity;
import org.sopt.pawkey.backendapi.domain.user.application.service.UserWrittenPostQueryService;
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
public class UserWrittenPostQueryFacade {

	private final UserQueryRepository userQueryRepository;
	private final UserWrittenPostQueryService userWrittenPostQueryService;

	public List<PostCardResponseDto> getMyPosts(Long userId) {
		UserEntity user = userQueryRepository.getUserByUserId(userId)
			.orElseThrow(() -> new UserBusinessException(UserErrorCode.USER_NOT_FOUND));

		List<PostEntity> posts = userWrittenPostQueryService.findMyPosts(user);
		List<Long> likedPostIds = userWrittenPostQueryService.getLikedPostIds(userId);

		return posts.stream()
			.map(post -> {
				String regionName = post.getRoute().getRegion().getParent().getRegionName() + " " +
					post.getRoute().getRegion().getRegionName();

				int durationMinutes = (int)(post.getRoute().getDuration() / 60);

				boolean isLiked = likedPostIds.contains(post.getPostId());

				return new PostCardResponseDto(
					post.getPostId(),
					regionName,
					post.getTitle(),
					post.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")),
					durationMinutes,
					isLiked,
					post.getRoute().getTrackingImage().getImageUrl()
				);
			})
			.toList();
	}

}