package org.sopt.pawkey.backendapi.domain.routes.api.dto.response;

import org.sopt.pawkey.backendapi.domain.routes.application.dto.result.FinishWalkResult;

import java.time.LocalDateTime;

public record SaveRouteResponseDTO(Long routeId,
                                   PetProfileDTO petProfile,
                                   WalkInfoDTO walkInfo) {
    public static SaveRouteResponseDTO from(FinishWalkResult result) {

        return new SaveRouteResponseDTO(result.routeId(),
                new PetProfileDTO(result.petName(), result.petImageUrl()),
                new WalkInfoDTO(result.startedAt()));


    }
    public record PetProfileDTO(String name, String imageUrl) {}

    public record WalkInfoDTO(LocalDateTime startedAt) {}
}

