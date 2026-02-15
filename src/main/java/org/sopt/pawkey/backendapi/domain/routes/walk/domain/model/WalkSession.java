package org.sopt.pawkey.backendapi.domain.routes.walk.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.locationtech.jts.geom.LineString;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class WalkSession {
    private final String routeId;
    private final Long userId;
    private final LocalDateTime startedAt;

    private LocalDateTime endedAt;
    private WalkSessionStatus status;

    private final List<WalkPoint> points = new ArrayList<>();

    public boolean isValid() {
        return points != null && points.size() >= 2;
    }

    public LineString toLineString() {
        // 좌표 → LineString 변환
    }
}
