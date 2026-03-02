package org.sopt.pawkey.backendapi.domain.recommendation.application.dto.result;

import org.sopt.pawkey.backendapi.domain.routes.infra.persistence.entity.RouteEntity;

public record RecommendationResult(Long routeId, String regionName, Double distanceKm, Integer durationMinutes, Long score) {
    public static RecommendationResult of(RouteEntity route, Long score) {
        return new RecommendationResult(
                route.getRouteId(),
                route.getRegion().getFullRegionName(),
                route.getDistance() / 1000.0,
                route.getDurationMinutes(),
                score
        );
    }
}
