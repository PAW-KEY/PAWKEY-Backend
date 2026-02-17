package org.sopt.pawkey.backendapi.domain.routes.walk.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.locationtech.jts.geom.LineString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class WalkSession {
    private final String routeId;
    private final Long userId;
    private final LocalDateTime startedAt;

    private LocalDateTime endedAt;
    private WalkSessionStatus status;

    private final List<WalkPoint> points = new ArrayList<>();


    public WalkSession(String routeId, Long userId, LocalDateTime startedAt) {
        this.routeId = routeId;
        this.userId = userId;
        this.startedAt = startedAt;
        this.status = WalkSessionStatus.ACTIVE;
    }

    public void end() {
        this.status = WalkSessionStatus.ENDED;
        this.endedAt = LocalDateTime.now();
    }
    public boolean isValid() {
        return points.size() >= 2;
    }

}
