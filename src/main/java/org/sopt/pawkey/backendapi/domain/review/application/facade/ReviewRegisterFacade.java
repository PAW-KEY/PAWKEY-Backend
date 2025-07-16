package org.sopt.pawkey.backendapi.domain.review.application.facade;

import org.sopt.pawkey.backendapi.domain.review.application.dto.command.ReviewRegisterCommand;
import org.sopt.pawkey.backendapi.domain.review.application.service.ReviewCachingService;
import org.sopt.pawkey.backendapi.domain.review.application.service.ReviewService;
import org.sopt.pawkey.backendapi.domain.review.infra.persistence.entity.ReviewCategoryOptionTop3Entity;
import org.sopt.pawkey.backendapi.domain.review.infra.persistence.entity.ReviewEntity;
import org.sopt.pawkey.backendapi.domain.routes.application.service.RouteService;
import org.sopt.pawkey.backendapi.domain.routes.infra.persistence.entity.RouteEntity;
import org.sopt.pawkey.backendapi.domain.user.application.service.UserService;
import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.UserEntity;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ReviewRegisterFacade {

	private final ReviewService reviewService;
	private final UserService userService;
	private final RouteService routeService;
	private final ReviewCachingService reviewCachingService;

	public void execute(Long userId,
		ReviewRegisterCommand command) {

		UserEntity user = userService.findById(userId);
		RouteEntity route = routeService.getRouteById(command.routeId());
		ReviewEntity review = reviewService.saveReview(command, user, route); //리뷰 생성


		//Top3 캐싱 테이블 갱신
		reviewCachingService.recalculateTop3ByRoute(route);


	}
}
