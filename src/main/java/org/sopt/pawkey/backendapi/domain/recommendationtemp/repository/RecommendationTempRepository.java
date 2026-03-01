package org.sopt.pawkey.backendapi.domain.recommendationtemp.repository;

import org.sopt.pawkey.backendapi.domain.dbti.domain.model.DbtiType;
import org.sopt.pawkey.backendapi.domain.recommendationtemp.dto.RouteScoreProjection;

import java.util.List;

public interface RecommendationTempRepository {
    List<RouteScoreProjection> findTopRoutes(Long regionId, DbtiType dbtiType);
}
