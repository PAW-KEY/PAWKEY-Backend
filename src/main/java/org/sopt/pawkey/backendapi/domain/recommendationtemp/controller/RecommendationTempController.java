package org.sopt.pawkey.backendapi.domain.recommendationtemp.controller;

import lombok.RequiredArgsConstructor;
import org.sopt.pawkey.backendapi.domain.dbti.domain.model.DbtiType;
import org.sopt.pawkey.backendapi.domain.recommendationtemp.dto.TempRecommendationResponseDto;
import org.sopt.pawkey.backendapi.domain.recommendationtemp.service.RecommendationTempService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/home")
@RequiredArgsConstructor
public class RecommendationTempController {

    private final RecommendationTempService recommendationTempService;

    @GetMapping("/recommendation")
    public ResponseEntity<List<TempRecommendationResponseDto>> getRecommendation(
            @RequestParam Long regionId,
            @RequestParam DbtiType dbtiType
    ) {
        return ResponseEntity.ok(
                recommendationTempService.getRecommendation(regionId, dbtiType)
        );
    }
}