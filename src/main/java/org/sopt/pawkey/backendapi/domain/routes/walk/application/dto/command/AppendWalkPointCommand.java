package org.sopt.pawkey.backendapi.domain.routes.walk.application.dto.command;

public record AppendWalkPointCommand(Long userId,
                                     String routeId,
                                     double lat,
                                     double lng,
                                     long timestamp) {
}
