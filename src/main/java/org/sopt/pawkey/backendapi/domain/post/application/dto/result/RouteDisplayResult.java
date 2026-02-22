package org.sopt.pawkey.backendapi.domain.post.application.dto.result;

import lombok.Builder;
import org.sopt.pawkey.backendapi.domain.routes.infra.persistence.entity.RouteEntity;
import org.sopt.pawkey.backendapi.global.util.RouteTimeFormatter;

import java.util.List;

@Builder
public record RouteDisplayResult(
        Long routeId,
        String locationText,
        String dateTimeText,
        List<String> metaTagTexts,
        String routeImageUrl
) {
    public static RouteDisplayResult from(RouteEntity route, String routeImageUrl) {
        return RouteDisplayResult.builder()
                .routeId(route.getRouteId())
                .locationText(route.getRegion().getFullRegionName())
                .dateTimeText(RouteTimeFormatter.format(route.getStartedAt()))
                .metaTagTexts(List.of(
                        route.getDistance() / 1000.0 + "km",
                        route.getDurationMinutes() + "분",
                        route.getStepCount() + "걸음"
                ))
                .routeImageUrl(routeImageUrl)
                .build();
    }
}