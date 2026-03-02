package org.sopt.pawkey.backendapi.domain.recommendation.application.dto.command;

import org.sopt.pawkey.backendapi.domain.dbti.domain.model.DbtiType;

public record GetRecommendationCommand(Long regionId, DbtiType dbtiType) {
}
