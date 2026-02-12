package org.sopt.pawkey.backendapi.domain.user.application.service;

import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.sopt.pawkey.backendapi.domain.post.application.dto.result.GetPostCardResult;
import org.sopt.pawkey.backendapi.domain.post.domain.repository.PostLikeRepository;
import org.sopt.pawkey.backendapi.domain.post.domain.repository.PostQueryRepository;
import org.sopt.pawkey.backendapi.domain.post.domain.repository.PostRepository;
import org.sopt.pawkey.backendapi.domain.post.exception.PostBusinessException;
import org.sopt.pawkey.backendapi.domain.post.exception.PostErrorCode;
import org.sopt.pawkey.backendapi.domain.post.infra.persistence.entity.PostEntity;
import org.sopt.pawkey.backendapi.domain.review.domain.repository.ReviewRepository;
import org.sopt.pawkey.backendapi.domain.review.infra.persistence.entity.ReviewEntity;
import org.sopt.pawkey.backendapi.domain.user.api.dto.response.ReviewCardResponseDto;
import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.UserEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserWrittenPostQueryService {

	private final PostRepository postRepository;
	private final PostLikeRepository postLikeRepository;

	public List<GetPostCardResult> findMyPostResults(UserEntity user, List<Long> likedPostIds) {
		Set<Long> likedPostIdSet = new HashSet<>(likedPostIds);

		return postRepository.findAllByUser(user).stream()
			.sorted(Comparator.comparing(PostEntity::getPostId).reversed())
			.map(post -> {
				var route = post.getRoute();

				String regionName = (route != null && route.getRegion() != null)
					? route.getRegion().getFullRegionName()
					: null;

				int durationMinutes = (route != null) ? route.getDurationMinutes() : 0;

				String routeMapImageUrl = (route != null && route.getTrackingImage() != null)
					? route.getTrackingImage().getImageUrl()
					: null;

				return GetPostCardResult.builder()
					.postId(post.getPostId())
					.regionName(regionName)
					.title(post.getTitle())
					.createdAt(post.getCreatedAt())
					.durationMinutes(durationMinutes)
					.isLike(likedPostIdSet.contains(post.getPostId()))
					.routeMapImageUrl(routeMapImageUrl)
					.build();
			})
			.toList();
	}

	public List<Long> getLikedPostIds(Long userId) {
		return postLikeRepository.findLikedPostIdsByUserId(userId);
	}

	private final ReviewRepository reviewRepository;

	public List<ReviewCardResponseDto> getMyWrittenReviews(Long userId) {
		List<ReviewEntity> reviews = reviewRepository.findAllByUserId(userId);

		return reviews.stream()
			.map(review -> {
				PostEntity post = postRepository.findByRoute(review.getRoute())
					.orElseThrow(
						() -> new PostBusinessException(PostErrorCode.POST_NOT_FOUND));

				return ReviewCardResponseDto.fromReview(review, post);
			})
			.toList();
	}
}