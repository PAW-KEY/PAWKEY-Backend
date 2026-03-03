package org.sopt.pawkey.backendapi.domain.routes.api.dto.response;

import lombok.Builder;
import org.sopt.pawkey.backendapi.domain.routes.recommendation.application.dto.result.RecommendationResult;
import java.util.List;

@Builder
public record RecommendationResponseDTO(
        List<RecommendationDisplay> recommendations
) {

    public static RecommendationResponseDTO from(List<RecommendationResult> results) {
        return RecommendationResponseDTO.builder()
                .recommendations(
                        results.stream()
                                .map(result -> new RecommendationDisplay(
                                        result.routeId(),
                                        result.postId(),
                                        result.routeImageUrl(),
                                        result.locationText(),
                                        result.dateText(),
                                        result.durationText(),
                                        result.distance(),
                                        result.likeCount()
                                ))
                                .toList()
                )
                .build();
    }

    public record RecommendationDisplay(
            Long routeId,
            Long postId,
            String routeImageUrl,
            String locationText,
            String dateText,
            String durationText,
            int distance,
            Long likeCount
    ) {}
}