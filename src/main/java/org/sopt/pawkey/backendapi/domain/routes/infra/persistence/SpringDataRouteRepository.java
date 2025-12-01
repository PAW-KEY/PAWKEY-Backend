package org.sopt.pawkey.backendapi.domain.routes.infra.persistence;

import java.util.Optional;

import org.sopt.pawkey.backendapi.domain.routes.infra.persistence.entity.RouteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SpringDataRouteRepository extends JpaRepository<RouteEntity, Long> {

	@Query("SELECT r FROM RouteEntity r "
		+ "JOIN FETCH r.region rg "
		+ "LEFT JOIN FETCH rg.parent parentRegion "
		+ "LEFT JOIN FETCH r.trackingImage ti "
		+ "WHERE r.routeId = :routeId")
	Optional<RouteEntity> getByRouteId(@Param("routeId") Long routeId);
}
