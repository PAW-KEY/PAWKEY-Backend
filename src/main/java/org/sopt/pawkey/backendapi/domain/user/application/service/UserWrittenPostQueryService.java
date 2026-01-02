package org.sopt.pawkey.backendapi.domain.user.application.service;

import java.util.Comparator;
import java.util.List;

import org.sopt.pawkey.backendapi.domain.post.application.dto.result.GetPostCardResult;
import org.sopt.pawkey.backendapi.domain.post.domain.repository.PostLikeRepository;
import org.sopt.pawkey.backendapi.domain.post.domain.repository.PostRepository;
import org.sopt.pawkey.backendapi.domain.post.infra.persistence.entity.PostEntity;
import org.sopt.pawkey.backendapi.domain.post.infra.persistence.entity.PostLikeEntity;
import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.UserEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserWrittenPostQueryService {

	private final PostRepository postRepository;
	private final PostLikeRepository postLikeRepository;

	public List<GetPostCardResult> findMyPostResults(UserEntity user, List<Long> likedPostIds) {
		return postRepository.findAllByUser(user).stream()
			.sorted(Comparator.comparing(PostEntity::getPostId).reversed())
			.map(post -> {
				String regionName = post.getRoute().getRegion().getParent().getRegionName() + " " +
					post.getRoute().getRegion().getRegionName();

				int durationMinutes = (int)(post.getRoute().getDuration() / 60);

				return GetPostCardResult.builder()
					.postId(post.getPostId())
					.regionName(regionName)
					.title(post.getTitle())
					.createdAt(post.getCreatedAt())
					.durationMinutes(durationMinutes)
					.isLike(likedPostIds.contains(post.getPostId()))
					.routeMapImageUrl(post.getRoute().getTrackingImage().getImageUrl())
					.build();
			})
			.toList();
	}

	public List<Long> getLikedPostIds(Long userId) {
		return postLikeRepository.findLikedPostIdsByUserId(userId);
	}
}