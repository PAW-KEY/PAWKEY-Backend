package org.sopt.pawkey.backendapi.domain.review.infra.persistence;

import static org.sopt.pawkey.backendapi.domain.region.infra.persistence.entity.QRegionEntity.*;
import static org.sopt.pawkey.backendapi.domain.review.infra.persistence.entity.QReviewEntity.*;
import static org.sopt.pawkey.backendapi.domain.routes.infra.persistence.entity.QRouteEntity.*;

import java.util.List;

import org.sopt.pawkey.backendapi.domain.review.domain.repository.ReviewRepository;
import org.sopt.pawkey.backendapi.domain.review.infra.persistence.entity.ReviewEntity;
import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ReviewRepositoryImpl implements ReviewRepository {

	private final SpringDataReviewRepository jpaRepository;

	@Override
	public void save(ReviewEntity review) {
		jpaRepository.save(review);
	}

	private final JPAQueryFactory query;

	@Override
	public List<ReviewEntity> findAllByUserId(Long userId) {
		return query.selectFrom(reviewEntity)
			.join(reviewEntity.route, routeEntity).fetchJoin()
			.join(routeEntity.region, regionEntity).fetchJoin()
			.where(reviewEntity.user.userId.eq(userId))
			.orderBy(reviewEntity.reviewId.desc())
			.fetch();
	}

	@Override
	public void deleteByUserId(Long userId) {
		jpaRepository.deleteByUserId(userId);
	}
}
