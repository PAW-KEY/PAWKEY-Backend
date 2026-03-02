package org.sopt.pawkey.backendapi.domain.routes.recommendation.application.dto.command;

import lombok.Builder;
import org.sopt.pawkey.backendapi.domain.dbti.domain.model.DbtiType;

@Builder
public record GetRecommendationCommand(
        Long userId
) {
    public static GetRecommendationCommand from(Long userId) {
        return GetRecommendationCommand.builder()
                .userId(userId)
                .build();
    }
}