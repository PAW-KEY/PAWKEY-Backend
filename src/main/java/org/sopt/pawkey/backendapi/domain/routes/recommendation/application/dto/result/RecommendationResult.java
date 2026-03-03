package org.sopt.pawkey.backendapi.domain.routes.recommendation.application.dto.result;

import lombok.Builder;
import org.sopt.pawkey.backendapi.domain.routes.infra.persistence.entity.RouteEntity;

import java.time.format.DateTimeFormatter;
@Builder
public record RecommendationResult(
        Long routeId,
        Long postId,
        String routeImageUrl,
        String locationText,
        String dateText,
        String durationText,
        int distance,
        Long likeCount
) {
    public static RecommendationResult of(RouteEntity route, Long score, Long postId) {
        return RecommendationResult.builder()
                .routeId(route.getRouteId())
                .postId(postId)
                .routeImageUrl(route.getTrackingImage().getImageUrl())
                .locationText(route.getRegion().getFullRegionName())
                .dateText(route.getStartedAt().format(DateTimeFormatter.ofPattern("yy/MM/dd")))
                .durationText(String.format("%02dh %02dmin",
                        route.getDuration() / 3600,
                        (route.getDuration() % 3600) / 60))
                .distance(route.getDistance())
                .likeCount(score)
                .build();
    }
}