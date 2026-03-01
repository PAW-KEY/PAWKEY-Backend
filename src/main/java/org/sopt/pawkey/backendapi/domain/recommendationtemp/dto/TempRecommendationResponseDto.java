package org.sopt.pawkey.backendapi.domain.recommendationtemp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.sopt.pawkey.backendapi.domain.routes.infra.persistence.entity.RouteEntity;

@Getter
@AllArgsConstructor
public class TempRecommendationResponseDto {

    private Long routeId;
    private String regionName;
    private Double distanceKm;
    private Integer durationMinutes;
    private Long score;

    public static TempRecommendationResponseDto of(
            RouteEntity route,
            Long score
    ) {
        return new TempRecommendationResponseDto(
                route.getRouteId(),
                route.getRegion().getFullRegionName(),
                route.getDistance() / 1000.0,
                route.getDurationMinutes(),
                score
        );
    }
}