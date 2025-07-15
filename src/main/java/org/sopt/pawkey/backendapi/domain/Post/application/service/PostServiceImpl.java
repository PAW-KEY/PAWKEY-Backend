package org.sopt.pawkey.backendapi.domain.post.application.service;

import java.util.List;

import org.sopt.pawkey.backendapi.domain.category.infra.persistence.entity.CategoryOptionEntity;
import org.sopt.pawkey.backendapi.domain.image.domain.model.ImageType;
import org.sopt.pawkey.backendapi.domain.image.infra.persistence.entity.ImageEntity;
import org.sopt.pawkey.backendapi.domain.post.application.dto.command.PostRegisterCommand;
import org.sopt.pawkey.backendapi.domain.post.domain.repository.PostRepository;
import org.sopt.pawkey.backendapi.domain.post.exception.PostBusinessException;
import org.sopt.pawkey.backendapi.domain.post.exception.PostErrorCode;
import org.sopt.pawkey.backendapi.domain.post.infra.persistence.entity.PostCategoryOptionTop3Entity;
import org.sopt.pawkey.backendapi.domain.post.infra.persistence.entity.PostEntity;
import org.sopt.pawkey.backendapi.domain.post.infra.persistence.entity.PostImageEntity;
import org.sopt.pawkey.backendapi.domain.post.infra.persistence.repository.PostCategoryOptionTop3Repository;
import org.sopt.pawkey.backendapi.domain.review.domain.repository.ReviewSelectedCategoryOptionRepository;
import org.sopt.pawkey.backendapi.domain.routes.domain.repository.RouteRepository;
import org.sopt.pawkey.backendapi.domain.routes.infra.persistence.entity.RouteEntity;
import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.UserEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

	private final PostRepository postRepository;
	private final RouteRepository routeRepository;
	private final ReviewSelectedCategoryOptionRepository reviewSelectedCategoryOptionRepository;
	private final PostCategoryOptionTop3Repository postCategoryOptionTop3Repository;


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

			post.getPostImageEntityList().add(postImage);
		}
		postRepository.save(post);
		return post;
	}

	@Override
	public void updateTop3CategoryOptionsFor(PostEntity post) {
		Long routeId = post.getRoute().getRouteId();
		List<Object[]> optionCountList = reviewSelectedCategoryOptionRepository.countOptionGroupByCategory(routeId);

		List<Object[]> top3 = optionCountList.stream()
			.sorted((a, b) -> Long.compare((Long)b[1], (Long)a[1]))
			.limit(3)
			.toList();

		postCategoryOptionTop3Repository.deleteByPost(post);
		for (Object[] record : top3) {
			CategoryOptionEntity option = (CategoryOptionEntity) record[0];
			Long count = (Long) record[1];

			PostCategoryOptionTop3Entity entity = PostCategoryOptionTop3Entity.builder()
				.post(post)
				.categoryOption(option)
				.selectionCount(count.intValue())
				.build();

			postCategoryOptionTop3Repository.updateReviewTop3(entity);
		}





	}

}

