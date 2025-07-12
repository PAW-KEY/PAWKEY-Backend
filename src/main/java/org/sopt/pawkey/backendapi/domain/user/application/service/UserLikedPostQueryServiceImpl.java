package org.sopt.pawkey.backendapi.domain.user.application.service;

import java.util.List;

import org.sopt.pawkey.backendapi.domain.post.domain.repository.PostLikeRepository;
import org.sopt.pawkey.backendapi.domain.post.infra.persistence.entity.PostLikeEntity;
import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.UserEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserLikedPostQueryServiceImpl implements UserLikedPostQueryService {

	private final PostLikeRepository postLikeRepository;

	@Override
	public List<PostLikeEntity> findLikedPostsByUser(UserEntity user) {
		return postLikeRepository.findAllByUser(user);
	}
}
