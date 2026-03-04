package org.sopt.pawkey.backendapi.domain.routes.recommendation;

import lombok.RequiredArgsConstructor;
import org.sopt.pawkey.backendapi.domain.routes.recommendation.application.service.RecommendationBatchService;
import org.sopt.pawkey.backendapi.domain.routes.recommendation.application.service.RecommendationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.sopt.pawkey.backendapi.global.constants.AppConstants.API_PREFIX;

@RestController
@RequestMapping("/internal")
@RequiredArgsConstructor
public class BatchTestController {
    private final RecommendationBatchService batchService;

    private final RecommendationService recommendationService;

    @GetMapping("/run-batch")
    public String runBatch() {
        batchService.refreshRouteRecoStats();
        return "배치 실행";
    }

    @GetMapping("/reload-redis")
    public String reloadRedis() {
        recommendationService.refreshRedisFromStats();
        return "Redis reload 완료";
    }
}

