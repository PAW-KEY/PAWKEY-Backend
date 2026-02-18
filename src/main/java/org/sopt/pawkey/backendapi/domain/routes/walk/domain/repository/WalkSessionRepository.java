package org.sopt.pawkey.backendapi.domain.routes.walk.domain.repository;

import org.sopt.pawkey.backendapi.domain.routes.walk.domain.model.WalkPoint;
import org.sopt.pawkey.backendapi.domain.routes.walk.domain.model.WalkSession;

public interface WalkSessionRepository {
    boolean existsActiveSession(Long userId);
    String createSession(Long userId, String deviceInfo, long startedAt);
    void bindActiveSession(Long userId, String routeId, long ttlSeconds);
    void appendPoint(String routeId, WalkPoint point);
    boolean isDuplicated(String routeId, long timestamp);
    WalkSession loadSession(String routeId);
    void endSession(String routeId);
    void clearActiveSession(Long userId);
}

