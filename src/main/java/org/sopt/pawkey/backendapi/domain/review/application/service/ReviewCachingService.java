package org.sopt.pawkey.backendapi.domain.review.application.service;

import org.sopt.pawkey.backendapi.domain.routes.infra.persistence.entity.RouteEntity;

public interface ReviewCachingService {


	void recalculateTop3ByRoute(RouteEntity route);
}
