package org.sopt.pawkey.backendapi.domain.user.application.service;

import java.util.List;

import org.sopt.pawkey.backendapi.domain.post.domain.repository.PostRepository;
import org.sopt.pawkey.backendapi.domain.post.infra.persistence.entity.PostEntity;
import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.UserEntity;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserWrittenPostQueryServiceImpl implements UserWrittenPostQueryService {

	private final PostRepository postRepository;

	@Override
	public List<PostEntity> findWrittenPostsByUser(UserEntity user) {
		return postRepository.findAllByUser(user);
	}
}
