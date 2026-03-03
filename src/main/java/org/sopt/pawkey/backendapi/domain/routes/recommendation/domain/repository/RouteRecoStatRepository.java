package org.sopt.pawkey.backendapi.domain.routes.recommendation.domain.repository;

import org.sopt.pawkey.backendapi.domain.dbti.domain.model.DbtiType;
import org.sopt.pawkey.backendapi.domain.routes.recommendation.infra.persistence.entity.RouteRecoStatEntity;

import java.util.List;

public interface RouteRecoStatRepository {
    List<RouteRecoStatEntity> findTop4(Long regionId, DbtiType dbtiType);
    void saveAll(List<RouteRecoStatEntity> stats);
    void deleteAllInBatch();
}
