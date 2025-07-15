package org.sopt.pawkey.backendapi.domain.post.infra.persistence.repository;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.sopt.pawkey.backendapi.domain.category.infra.persistence.entity.QCategoryOptionEntity;
import org.sopt.pawkey.backendapi.domain.image.infra.persistence.entity.QImageEntity;
import org.sopt.pawkey.backendapi.domain.pet.infra.persistence.entity.QPetEntity;
import org.sopt.pawkey.backendapi.domain.post.api.dto.request.FilterPostsRequestDto;
import org.sopt.pawkey.backendapi.domain.post.application.dto.result.GetPostResult;
import org.sopt.pawkey.backendapi.domain.post.domain.repository.PostQueryRepository;
import org.sopt.pawkey.backendapi.domain.post.infra.persistence.entity.PostEntity;
import org.sopt.pawkey.backendapi.domain.post.infra.persistence.entity.QPostEntity;
import org.sopt.pawkey.backendapi.domain.post.infra.persistence.entity.QPostImageEntity;
import org.sopt.pawkey.backendapi.domain.post.infra.persistence.entity.QPostLikeEntity;
import org.sopt.pawkey.backendapi.domain.post.infra.persistence.entity.QPostSelectedCategoryOptionEntity;
import org.sopt.pawkey.backendapi.domain.region.infra.persistence.entity.QRegionEntity;
import org.sopt.pawkey.backendapi.domain.routes.infra.persistence.entity.QRouteEntity;
import org.sopt.pawkey.backendapi.domain.routes.infra.persistence.entity.RouteEntity;
import org.sopt.pawkey.backendapi.domain.user.api.dto.AuthorDto;
import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.QUserEntity;
import org.springframework.stereotype.Repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class PostQueryRepositoryImpl implements PostQueryRepository {

	private final JPAQueryFactory query;

	QPostEntity post = QPostEntity.postEntity;
	QRouteEntity route = QRouteEntity.routeEntity;
	QPostSelectedCategoryOptionEntity sel = QPostSelectedCategoryOptionEntity.postSelectedCategoryOptionEntity;
	QCategoryOptionEntity opt = QCategoryOptionEntity.categoryOptionEntity;
	QRegionEntity region = QRegionEntity.regionEntity;
	QPostImageEntity image = QPostImageEntity.postImageEntity;
	QImageEntity imageEntity = QImageEntity.imageEntity;
	QPetEntity pet = QPetEntity.petEntity;
	QUserEntity user = QUserEntity.userEntity;
	QPostLikeEntity like = QPostLikeEntity.postLikeEntity;

	@Override
	public List<GetPostResult> findByFilter(FilterPostsRequestDto dto) {

		BooleanBuilder builder = new BooleanBuilder()
			.and(post.isPublic.isTrue())
			.and(route.duration.between(dto.durationStart(), dto.durationEnd()));

		dto.selectedOptions().forEach(cat -> {
			builder.and(JPAExpressions.selectOne()
				.from(sel)
				.join(sel.categoryOption, opt)
				.where(sel.post.eq(post),
					opt.category.categoryId.eq(cat.categoryId()),
					opt.id.in(cat.optionsIds()))
				.exists());
		});

		// fetch 사용은, 성능상 중요한 post, route, region, user, pet
		List<PostEntity> posts = query.selectFrom(post)
			.join(post.route, route).fetchJoin()
			.join(route.region, region).fetchJoin()
			.join(post.user, user).fetchJoin()
			.join(post.user.petEntityList, pet).fetchJoin()
			.where(builder)
			.orderBy(post.createdAt.desc())
			.fetch();

		// 후처리용 N+1 safe 조회
		Map<Long, List<String>> postIdToCategoryTags = getCategoryTagsMap(posts);
		Map<Long, List<String>> postIdToWalkingImages = getWalkingImageMap(posts);

		return posts.stream().map(p -> {
			Long postId = p.getPostId();
			RouteEntity r = p.getRoute();

			return new GetPostResult(
				postId,
				r.getRouteId(),
				p.getTitle(),
				p.getDescription(),
				true,
				new AuthorDto(p.getUser().getUserId(), p.getPet().getPetId(), p.getPet().getName(),
					p.getPet().getProfileImage().getImageUrl()),
				postIdToCategoryTags.getOrDefault(postId, List.of()),
				r.getRegion().getRegionName(),
				p.getCreatedAt(),
				r.getTrackingImage().getImageUrl(),
				postIdToWalkingImages.getOrDefault(postId, List.of())
			);
		}).toList();
	}

	private Map<Long, List<String>> getCategoryTagsMap(List<PostEntity> posts) {
		List<Tuple> results = query
			.select(post.postId, opt.optionSummary)
			.from(sel)
			.join(sel.post, post)
			.join(sel.categoryOption, opt)
			.where(post.in(posts))
			.fetch();

		return results.stream().collect(Collectors.groupingBy(
			t -> t.get(post.postId),
			Collectors.mapping(t -> t.get(opt.optionSummary), Collectors.toList())
		));
	}

	private Map<Long, List<String>> getWalkingImageMap(List<PostEntity> posts) {
		List<Tuple> results = query
			.select(post.postId, imageEntity.imageUrl)
			.from(image)
			.join(image.post, post)
			.join(image.image, imageEntity)
			.where(image.post.in(posts))
			.fetch();

		return results.stream().collect(Collectors.groupingBy(
			t -> t.get(post.postId),
			Collectors.mapping(t -> t.get(imageEntity.imageUrl), Collectors.toList())
		));
	}
}
