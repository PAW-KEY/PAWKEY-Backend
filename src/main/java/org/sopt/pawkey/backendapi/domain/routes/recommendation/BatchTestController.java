package org.sopt.pawkey.backendapi.domain.routes.recommendation;

import lombok.RequiredArgsConstructor;
import org.sopt.pawkey.backendapi.domain.routes.recommendation.application.service.RecommendationBatchService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.sopt.pawkey.backendapi.global.constants.AppConstants.API_PREFIX;

@RestController
@RequestMapping(API_PREFIX + "/internal")
@RequiredArgsConstructor
public class BatchTestController {
    private final RecommendationBatchService batchService;

    @GetMapping("/run-batch")
    public String runBatch() {
        batchService.refreshRouteRecoStats();
        return "배치 실행";
    }
}

