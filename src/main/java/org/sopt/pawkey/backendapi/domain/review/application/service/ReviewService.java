package org.sopt.pawkey.backendapi.domain.review.application.service;

import org.sopt.pawkey.backendapi.domain.review.application.dto.command.ReviewRegisterCommand;
import org.sopt.pawkey.backendapi.domain.review.infra.persistence.entity.ReviewEntity;
import org.sopt.pawkey.backendapi.domain.routes.infra.persistence.entity.RouteEntity;
import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.UserEntity;

public interface ReviewService {

	ReviewEntity saveReview(ReviewRegisterCommand command, UserEntity user, RouteEntity route);

}
