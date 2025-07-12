package org.sopt.pawkey.backendapi.domain.routes.infra.persistence;

import org.sopt.pawkey.backendapi.domain.routes.infra.persistence.entity.RouteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataRouteRepository extends JpaRepository<RouteEntity, Long> {
}
