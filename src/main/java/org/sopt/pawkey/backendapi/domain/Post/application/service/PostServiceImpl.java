package org.sopt.pawkey.backendapi.domain.post.application.service;

import java.util.List;

import org.sopt.pawkey.backendapi.domain.image.domain.model.ImageType;
import org.sopt.pawkey.backendapi.domain.image.infra.persistence.entity.ImageEntity;
import org.sopt.pawkey.backendapi.domain.post.application.dto.command.PostRegisterCommand;
import org.sopt.pawkey.backendapi.domain.post.domain.repository.PostRepository;
import org.sopt.pawkey.backendapi.domain.post.exception.PostBusinessException;
import org.sopt.pawkey.backendapi.domain.post.exception.PostErrorCode;
import org.sopt.pawkey.backendapi.domain.post.infra.persistence.entity.PostEntity;
import org.sopt.pawkey.backendapi.domain.post.infra.persistence.entity.PostImageEntity;
import org.sopt.pawkey.backendapi.domain.routes.domain.repository.RouteRepository;
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
	public PostEntity savePost(UserEntity writer,
		PostRegisterCommand command,
		RouteEntity route,
		List<ImageEntity> images) {

		PostEntity post = PostEntity.builder()
			.user(writer)
			.route(route)
			.title(command.title())
			.description(command.description())
			.isPublic(command.isPublic())
			.pet(writer.getPet())
			.build();

		for (ImageEntity image : images) {
			PostImageEntity postImage = PostImageEntity.builder()
				.imageType(ImageType.WALK_POST)
				.image(image)
				.post(post)
				.build();

			post.getPostImages().add(postImage);
		}
		postRepository.save(post);
		return post;
	}

}

