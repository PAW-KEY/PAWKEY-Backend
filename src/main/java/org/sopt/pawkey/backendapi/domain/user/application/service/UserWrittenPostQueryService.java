package org.sopt.pawkey.backendapi.domain.user.application.service;

import java.util.Comparator;
import java.util.List;

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

	public List<PostEntity> findMyPosts(UserEntity user) {
		return postRepository.findAllByUser(user)
			.stream()
			.sorted(Comparator.comparing(PostEntity::getPostId).reversed())
			.toList();
	}

	public List<Long> getLikedPostIds(Long userId) {
		return postLikeRepository.findLikedPostIdsByUserId(userId);
	}
}