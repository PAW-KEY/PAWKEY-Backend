package org.sopt.pawkey.backendapi.domain.coordinate;

import jakarta.validation.constraints.NotNull;

public record Coordinate(@NotNull(message = "경도는 필수입니다.") Double longitude,
						 @NotNull(message = "위도는 필수입니다.") Double latitude) {
}
