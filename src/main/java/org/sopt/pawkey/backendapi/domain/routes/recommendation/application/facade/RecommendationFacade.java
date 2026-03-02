package org.sopt.pawkey.backendapi.domain.routes.recommendation.application.facade;

import lombok.RequiredArgsConstructor;
import org.sopt.pawkey.backendapi.domain.routes.recommendation.application.dto.command.GetRecommendationCommand;
import org.sopt.pawkey.backendapi.domain.routes.recommendation.application.dto.result.RecommendationResult;
import org.sopt.pawkey.backendapi.domain.routes.recommendation.application.service.RecommendationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecommendationFacade {
    private final RecommendationService recommendationService;

    @Transactional(readOnly = true)
    public List<RecommendationResult> getHomeRecommendations(Long userId) {
        GetRecommendationCommand command = GetRecommendationCommand.from(userId);

        return recommendationService.getRecommendations(command);
    }
}