package org.sopt.pawkey.backendapi.domain.user.application.service;

import java.util.List;

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

	public List<PostLikeEntity> findLikedPostsByUserWithPostAndImages(Long userId) {
		return postLikeRepository.findAllByUserWithPostAndImages(userId);
	}
}
