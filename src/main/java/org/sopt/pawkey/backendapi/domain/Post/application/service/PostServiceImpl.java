package org.sopt.pawkey.backendapi.domain.post.application.service;

import org.sopt.pawkey.backendapi.domain.post.application.dto.command.PostCreateCommand;
import org.sopt.pawkey.backendapi.domain.post.domain.repository.PostRepository;
import org.sopt.pawkey.backendapi.domain.post.exception.PostBusinessException;
import org.sopt.pawkey.backendapi.domain.post.exception.PostErrorCode;
import org.sopt.pawkey.backendapi.domain.post.infra.persistence.entity.PostEntity;
import org.sopt.pawkey.backendapi.domain.routes.domain.RouteRepository;
import org.sopt.pawkey.backendapi.domain.routes.exception.RouteBusinessException;
import org.sopt.pawkey.backendapi.domain.routes.exception.RouteErrorCode;
import org.sopt.pawkey.backendapi.domain.routes.infra.persistence.entity.RouteEntity;
import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.UserEntity;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

	private final PostRepository postRepository;
	private final RouteRepository routeRepository;

	@Override
	public PostEntity findById(Long postId) {
		return postRepository.findById(postId)
			.orElseThrow(() -> new PostBusinessException(PostErrorCode.POST_NOT_FOUND));
	}

	@Override
	public void createPost(UserEntity writer, PostCreateCommand command) {

		RouteEntity route = routeRepository.getRouteByRouteId(command.getRouteId())
			.orElseThrow(() -> new RouteBusinessException(RouteErrorCode.ROUTE_NOT_FOUND));

		PostEntity post = PostEntity.builder()
			.user(writer)
			.route(route)
			.title(command.getTitle())
			.description(command.getDescription())
			.isPublic(command.isPublic())
			.build();
		postRepository.save(post);
	}

}

