package org.sopt.pawkey.backendapi.domain.routes.api.dto.request;

import org.sopt.pawkey.backendapi.domain.routes.application.dto.command.SaveRouteCommand;

import java.time.LocalDateTime;

public record SaveRouteRequestDTO(int distance,
                                  int duration,
                                  int stepCount,
                                  LocalDateTime endedAt) {
    public SaveRouteCommand toCommand(Long userId, String routeId) {
        return new SaveRouteCommand(userId, routeId, distance, duration, stepCount, endedAt);
    }
}
