package org.sopt.pawkey.backendapi.domain.routes.application.dto.command;

import java.time.LocalDateTime;

public record SaveRouteCommand(Long userId,
                               String routeId,
                               int distance,
                               int duration,
                               int stepCount,
                               LocalDateTime endedAt) {
}
