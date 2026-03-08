package org.sopt.pawkey.backendapi.domain.routes.api.dto.response;

import org.sopt.pawkey.backendapi.domain.routes.application.dto.result.FinishWalkResult;
import org.sopt.pawkey.backendapi.global.util.RouteTimeFormatter;

import java.time.LocalDateTime;

public record SaveRouteResponseDTO(Long routeId,
                                   PetProfileDTO petProfile,
                                   WalkInfoDTO walkInfo) {
    public static SaveRouteResponseDTO from(FinishWalkResult result) {

        return new SaveRouteResponseDTO(result.routeId(),
                new PetProfileDTO(result.petName(), result.petImageUrl()),
                WalkInfoDTO.from(result.startedAt()));
    }
    public record PetProfileDTO(String name, String imageUrl) {}

    public record WalkInfoDTO(String  startedAt) {
        public static WalkInfoDTO from(LocalDateTime startedAt){
            return new WalkInfoDTO(RouteTimeFormatter.format(startedAt));
        }
    }
}

