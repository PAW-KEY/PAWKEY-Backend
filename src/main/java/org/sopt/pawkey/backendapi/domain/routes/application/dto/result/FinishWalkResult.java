package org.sopt.pawkey.backendapi.domain.routes.application.dto.result;

import org.sopt.pawkey.backendapi.domain.routes.infra.persistence.entity.RouteEntity;

import java.time.LocalDateTime;

public record FinishWalkResult(Long routeId,
                               String petName,
                               String petImageUrl,
                               LocalDateTime startedAt) {
    public static FinishWalkResult from(RouteEntity route) {
        return new FinishWalkResult(
                route.getRouteId(),
                route.getUser().getPet().getName(),
                route.getUser().getPet().getProfileImage().getImageUrl(),
                route.getStartedAt()
        );
    }
}
