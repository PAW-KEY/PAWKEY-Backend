package org.sopt.pawkey.backendapi.domain.routes.api.dto.response;

import lombok.Builder;
import org.sopt.pawkey.backendapi.domain.routes.application.dto.result.GetRouteSummaryResult;

import java.util.List;

@Builder
public record GetRouteSummaryResponseDto(
        RouteDisplay routeDisplay
) {
    public static GetRouteSummaryResponseDto from(GetRouteSummaryResult resultDto) {
        return GetRouteSummaryResponseDto.builder()
                .routeDisplay(
                        new RouteDisplay(
                                resultDto.routeId(),
                                resultDto.locationText(),
                                resultDto.dateTimeText(),
                                resultDto.metaTagTexts()
                        )
                )
                .build();
    }

    public record RouteDisplay(
            Long routeId,
            String locationText,
            String dateTimeText,
            List<String> metaTagTexts
    ) {}
}