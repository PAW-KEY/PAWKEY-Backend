package org.sopt.pawkey.backendapi.domain.post.infra.persistence.repository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.sopt.pawkey.backendapi.domain.image.infra.persistence.entity.QImageEntity;
import org.sopt.pawkey.backendapi.domain.post.api.dto.request.FilterPostsRequestDto;
import org.sopt.pawkey.backendapi.domain.post.application.dto.result.GetPostCardResult;
import org.sopt.pawkey.backendapi.domain.post.domain.repository.PostQueryRepository;
import org.sopt.pawkey.backendapi.domain.post.exception.PostBusinessException;
import org.sopt.pawkey.backendapi.domain.post.exception.PostErrorCode;
import org.sopt.pawkey.backendapi.domain.post.infra.persistence.entity.PostEntity;
import org.sopt.pawkey.backendapi.domain.post.infra.persistence.entity.QPostEntity;
import org.sopt.pawkey.backendapi.domain.post.infra.persistence.entity.QPostLikeEntity;
import org.sopt.pawkey.backendapi.domain.post.infra.persistence.entity.QPostSelectedCategoryOptionEntity;
import org.sopt.pawkey.backendapi.domain.region.infra.persistence.entity.QRegionEntity;
import org.sopt.pawkey.backendapi.domain.routes.infra.persistence.entity.QRouteEntity;
import org.springframework.stereotype.Repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
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
	QRegionEntity region = QRegionEntity.regionEntity;
	QPostLikeEntity postLike = QPostLikeEntity.postLikeEntity;

	QImageEntity routeTrackingImage = new QImageEntity("routeTrackingImage");

	private static final Long DURATION_CATEGORY_ID = 6L;

	@Override
	public List<GetPostCardResult> findByFilter(FilterPostsRequestDto dto, String sortBy, String cursor, int size,
		Long userId) {

		BooleanBuilder builder = new BooleanBuilder()
			.and(post.isPublic.isTrue());

		if (dto.selectedOptions() != null) {
			for (var filter : dto.selectedOptions()) {
				Long targetId = filter.getTargetId();
				List<Long> optionIds = filter.getOptionIdList();

				if (DURATION_CATEGORY_ID.equals(targetId)) {
					builder.and(durationFilter(optionIds));
				} else {
					builder.and(categoryFilter(optionIds));
				}
			}
		}

		// 메인 쿼리
		List<PostEntity> posts = query.selectFrom(post)
			.join(post.route, route).fetchJoin()
			.join(route.region, region).fetchJoin()
			.leftJoin(route.trackingImage, routeTrackingImage).fetchJoin()
			.where(
				builder,
				cursorCondition(sortBy, cursor)
			)
			.orderBy(getOrderSpecifiers(sortBy))
			.limit(size + 1)
			.fetch();

		return convertToResult(posts, userId);
	}

	private BooleanExpression cursorCondition(String sortBy, String cursor) {
		if (cursor == null || cursor.isBlank())
			return null;

		try {
			if ("popular".equals(sortBy)) { // 인기순 커서 파싱 (좋아요수_ID)
				String[] parts = cursor.split("_");

				if (parts.length < 2) {
					throw new PostBusinessException(PostErrorCode.INVALID_CURSOR_FORMAT);
				}

				long cursorLikeCount = Long.parseLong(parts[0]);
				long cursorPostId = Long.parseLong(parts[1]);

				// 좋아요가 더 적거나, 좋아요가 같으면 ID가 더 작은 것 조회
				return post.likeCount.lt(cursorLikeCount)
					.or(post.likeCount.eq(cursorLikeCount).and(post.postId.lt(cursorPostId)));
			}

			// 최신순 (Latest): ID 기준 내림차순
			return post.postId.lt(Long.parseLong(cursor));
		} catch (NumberFormatException e) {
			throw new PostBusinessException(PostErrorCode.INVALID_CURSOR_FORMAT);
		}
	}

	private static final long DURATION_ALL = 21L;      // 시간 무관
	private static final long DURATION_UNDER_30 = 22L; // 30분 미만 (1800초)
	private static final long DURATION_30_TO_60 = 23L; // 30~60분 (1800~3600초)
	private static final long DURATION_OVER_60 = 24L;  // 1시간 이상 (3600초~)

	private BooleanBuilder durationFilter(List<Long> optionIds) {
		if (optionIds.isEmpty() || optionIds.contains(DURATION_ALL))
			return null;

		BooleanBuilder durBuilder = new BooleanBuilder();
		for (Long id : optionIds) {
			if (id == null)
				continue;
			if (id == DURATION_UNDER_30)
				durBuilder.or(route.duration.lt(1800));
			else if (id == DURATION_30_TO_60)
				durBuilder.or(route.duration.goe(1800).and(route.duration.lt(3600)));
			else if (id == DURATION_OVER_60)
				durBuilder.or(route.duration.goe(3600));
		}
		return durBuilder;
	}

	private BooleanExpression categoryFilter(List<Long> optionIds) {
		return JPAExpressions.selectOne()
			.from(sel)
			.where(
				sel.post.eq(post),
				sel.categoryOption.id.in(optionIds)
			).exists(); // EXISTS 서브쿼리를 사용하여 중복 데이터 발생을 차단
	}

	/**
	 * OrderSpecifier는 QueryDSL에서 정렬 조건을 정의하는 객체
	 * <?>는 제네릭 와일드카드로, 정렬 대상 컬럼의 데이터 타입(Long, String 등)에 관계없이 모두 수용하겠다는 의미
	 */
	private OrderSpecifier<?>[] getOrderSpecifiers(String sortBy) {
		if ("popular".equals(sortBy)) {
			return new OrderSpecifier[] {
				post.likeCount.desc(),
				post.postId.desc()
			};
		}
		return new OrderSpecifier[] {post.postId.desc()};
	}

	private List<GetPostCardResult> convertToResult(List<PostEntity> posts, Long userId) {
		Set<Long> likedPostIds = getLikedPostIds(userId, posts);

		return posts.stream().map(p -> GetPostCardResult.builder()
			.postId(p.getPostId())
			.regionName(p.getRoute().getRegion().getFullRegionName())
			.title(p.getTitle())
			.createdAt(p.getCreatedAt())
			.durationMinutes((int)(p.getRoute().getDuration() / 60)) // 분 단위
			.isLike(likedPostIds.contains(p.getPostId()))
			.routeMapImageUrl(p.getRoute().getTrackingImage() != null
				? p.getRoute().getTrackingImage().getImageUrl() : null)
			.likeCount(p.getLikeCount())
			.build()).toList();
	}
	private Set<Long> getLikedPostIds(Long userId, List<PostEntity> posts) {
		if (userId == null || posts.isEmpty())
			return Set.of();
		return query.select(postLike.post.postId)
			.from(postLike)
			.where(postLike.user.userId.eq(userId), postLike.post.in(posts))
			.fetch().stream().collect(Collectors.toSet());
	}
}