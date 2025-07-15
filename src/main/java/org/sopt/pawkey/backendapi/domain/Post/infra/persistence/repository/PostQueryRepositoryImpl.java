package org.sopt.pawkey.backendapi.domain.post.infra.persistence.repository;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.sopt.pawkey.backendapi.domain.category.infra.persistence.entity.QCategoryOptionEntity;
import org.sopt.pawkey.backendapi.domain.image.infra.persistence.entity.QImageEntity;
import org.sopt.pawkey.backendapi.domain.pet.infra.persistence.entity.QPetEntity;
import org.sopt.pawkey.backendapi.domain.post.api.dto.request.FilterPostsRequestDto;
import org.sopt.pawkey.backendapi.domain.post.application.dto.result.GetPostCardResult;
import org.sopt.pawkey.backendapi.domain.post.domain.repository.PostQueryRepository;
import org.sopt.pawkey.backendapi.domain.post.infra.persistence.entity.PostEntity;
import org.sopt.pawkey.backendapi.domain.post.infra.persistence.entity.QPostEntity;
import org.sopt.pawkey.backendapi.domain.post.infra.persistence.entity.QPostImageEntity;
import org.sopt.pawkey.backendapi.domain.post.infra.persistence.entity.QPostLikeEntity;
import org.sopt.pawkey.backendapi.domain.post.infra.persistence.entity.QPostSelectedCategoryOptionEntity;
import org.sopt.pawkey.backendapi.domain.region.infra.persistence.entity.QRegionEntity;
import org.sopt.pawkey.backendapi.domain.routes.infra.persistence.entity.QRouteEntity;
import org.sopt.pawkey.backendapi.domain.user.api.dto.AuthorDto;
import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.QUserEntity;
import org.springframework.stereotype.Repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

/**
 * 게시글(Post) 조회 전용 커스텀 리포지토리 구현체.
 * <p>
 *  - QueryDSL을 사용하여 동적 쿼리를 타입 세이프하게 작성.
 *  - FilterPostsRequestDto에 담긴 조건을 기반으로 게시글을 필터링.
 *  - fetch join을 활용해 N+1 문제를 방지 밒 성능 최적화.
 *  - 카테고리 태그 / 산책 이미지 등 후처리 조회가 필요한 데이터는 별도 메서드에서 일괄 조회.
 *  - 최종적으로 API 응답 전용 DTO(GetPostResult) 형태로 변환해서 반환.
 */
@Repository
@RequiredArgsConstructor
public class PostQueryRepositoryImpl implements PostQueryRepository {

	// QueryDSL의 핵심. EntityManager를 주입받아 쿼리 빌더로 사용.
	private final JPAQueryFactory query;

	// === Q 클래스 인스턴스 (QueryDSL이 생성한 메타모델) ===
	QPostEntity post = QPostEntity.postEntity;
	QRouteEntity route = QRouteEntity.routeEntity;
	QPostSelectedCategoryOptionEntity sel = QPostSelectedCategoryOptionEntity.postSelectedCategoryOptionEntity;
	QCategoryOptionEntity opt = QCategoryOptionEntity.categoryOptionEntity;
	QRegionEntity region = QRegionEntity.regionEntity;
	QPostImageEntity image = QPostImageEntity.postImageEntity;
	QImageEntity imageEntity = QImageEntity.imageEntity;
	QPetEntity pet = QPetEntity.petEntity;
	QUserEntity user = QUserEntity.userEntity;
	QPostLikeEntity postLike = QPostLikeEntity.postLikeEntity;

	@Override
	public List<GetPostCardResult> findByFilter(FilterPostsRequestDto dto, Long userId) {

		BooleanBuilder builder = new BooleanBuilder()
			.and(post.isPublic.isTrue());

		// duration이 null이 아닐 경우만 필터링 (분 → 초 변환)
		if (dto.durationStart() != null && dto.durationEnd() != null) {
			builder.and(route.duration.between(
				dto.durationStart() * 60,
				dto.durationEnd() * 60
			));
		}

		// 선택된 카테고리 옵션만 필터링 조건에 추가
		if (dto.selectedOptions() != null) {
			dto.selectedOptions().forEach(cat -> {
				if (cat.optionsIds() != null && !cat.optionsIds().isEmpty()) {
					builder.and(JPAExpressions.selectOne()
						.from(sel)
						.join(sel.categoryOption, opt)
						.where(
							sel.post.eq(post),
							opt.category.categoryId.eq(cat.categoryId()),
							opt.id.in(cat.optionsIds())
						).exists());
				}
			});
		}

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

		Set<Long> likedPostIds = getLikedPostIds(userId, posts); // 좋아요 정보 조회

		// 4) Entity -> DTO 변환
		return posts.stream().map(p -> {
			Long postId = p.getPostId();

			return GetPostCardResult.builder()
				.postId(postId)
				.title(p.getTitle())
				.isLike(likedPostIds.contains(postId))
				.author(new AuthorDto(
					p.getUser().getUserId(),
					p.getPet().getPetId(),
					p.getPet().getName(),
					p.getPet().getProfileImage().getImageUrl()
				))
				.categoryTags(postIdToCategoryTags.getOrDefault(postId, List.of()))
				.createdAt(p.getCreatedAt())
				.routeMapImageUrl(p.getRoute().getTrackingImage().getImageUrl())
				.build();
		}).toList();
	}

	// --------------------------------------------------------------------------------
	// (private) 카테고리 태그 일괄 조회
	// ---------------------------------------------------------------------------
	private Map<Long, List<String>> getCategoryTagsMap(List<PostEntity> posts) {
		List<Tuple> results = query
			.select(post.postId, opt.optionSummary)
			.from(sel)
			.join(sel.post, post)
			.join(sel.categoryOption, opt)
			.where(post.in(posts))
			.fetch();

		// 결과를 postId 기준 Map으로 그룹핑
		return results.stream().collect(Collectors.groupingBy(
			t -> t.get(post.postId),
			Collectors.mapping(t -> t.get(opt.optionSummary), Collectors.toList())
		));
	}

	private Set<Long> getLikedPostIds(Long userId, List<PostEntity> posts) {
		return query
			.select(postLike.post.postId)
			.from(postLike)
			.where(
				postLike.user.userId.eq(userId),
				postLike.post.in(posts)
			)
			.fetch()
			.stream()
			.collect(Collectors.toSet());
	}
}
