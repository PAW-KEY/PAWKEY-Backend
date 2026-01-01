package org.sopt.pawkey.backendapi.domain.routes.walktest.dto;

public record WalkPointRequest(String routeId,
							   double lat,
							   double lng,
							   long timestamp) {
}
