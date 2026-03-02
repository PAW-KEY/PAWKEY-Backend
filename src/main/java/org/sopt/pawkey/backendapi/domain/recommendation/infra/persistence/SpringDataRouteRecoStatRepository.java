package org.sopt.pawkey.backendapi.domain.recommendation.infra.persistence;

import org.sopt.pawkey.backendapi.domain.dbti.domain.model.DbtiType;
import org.sopt.pawkey.backendapi.domain.recommendation.infra.persistence.entity.RouteRecoStatEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpringDataRouteRecoStatRepository extends JpaRepository<RouteRecoStatEntity, Long> {
    List<RouteRecoStatEntity> findTop4ByRegionIdAndDbtiTypeOrderByScoreDesc(Long regionId, DbtiType dbtiType);
}
