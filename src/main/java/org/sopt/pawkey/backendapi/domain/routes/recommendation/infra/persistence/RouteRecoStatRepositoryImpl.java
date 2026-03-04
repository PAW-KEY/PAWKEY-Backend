package org.sopt.pawkey.backendapi.domain.routes.recommendation.infra.persistence;

import lombok.RequiredArgsConstructor;
import org.sopt.pawkey.backendapi.domain.dbti.domain.model.DbtiType;
import org.sopt.pawkey.backendapi.domain.routes.recommendation.domain.repository.RouteRecoStatRepository;
import org.sopt.pawkey.backendapi.domain.routes.recommendation.infra.persistence.entity.RouteRecoStatEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class RouteRecoStatRepositoryImpl implements RouteRecoStatRepository {
    private final SpringDataRouteRecoStatRepository jpaRepository;

    @Override
    public List<RouteRecoStatEntity> findTop4(Long regionId, DbtiType dbtiType) {
        return jpaRepository.findTop4ByRegionIdAndDbtiTypeOrderByScoreDesc(regionId, dbtiType);
    }

    @Override
    public void saveAll(List<RouteRecoStatEntity> stats) {
        jpaRepository.saveAll(stats);
    }

    @Override
    public void deleteAllInBatch() {
        jpaRepository.deleteAllInBatch();
    }

    @Override
    public List<RouteRecoStatEntity> findAll() {
        return jpaRepository.findAll();
    }
}
