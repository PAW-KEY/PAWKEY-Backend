package org.sopt.pawkey.backendapi.domain.user.application.service;

import java.util.Comparator;
import java.util.List;

import org.sopt.pawkey.backendapi.domain.post.application.dto.result.GetPostCardResult;
import org.sopt.pawkey.backendapi.domain.post.domain.repository.PostLikeRepository;
import org.sopt.pawkey.backendapi.domain.post.infra.persistence.entity.PostLikeEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserLikedPostQueryService {

	private final PostLikeRepository postLikeRepository;

	public List<GetPostCardResult> findLikedPostResults(Long userId) {
		return postLikeRepository.findAllByUserWithPostAndImages(userId).stream()
			.sorted(Comparator.comparing(PostLikeEntity::getPostLikeId).reversed())
			.filter(postLike -> postLike.getPost().isPublic())
			.map(postLike -> {
				var post = postLike.getPost();
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
					.isLike(true)
					.routeMapImageUrl(routeMapImageUrl)
					.build();
			})
			.toList();
	}
}