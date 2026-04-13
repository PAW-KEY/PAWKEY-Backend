package org.sopt.pawkey.backendapi.domain.routes.application.service;

import java.util.List;
import java.util.Optional;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.PrecisionModel;

import org.sopt.pawkey.backendapi.domain.image.infra.persistence.entity.ImageEntity;

import org.sopt.pawkey.backendapi.domain.routes.application.dto.command.SaveRouteCommand;
import org.sopt.pawkey.backendapi.domain.routes.domain.repository.RouteRepository;
import org.sopt.pawkey.backendapi.domain.routes.exception.RouteBusinessException;
import org.sopt.pawkey.backendapi.domain.routes.exception.RouteErrorCode;
import org.sopt.pawkey.backendapi.domain.routes.infra.persistence.entity.RouteEntity;
import org.sopt.pawkey.backendapi.domain.routes.walk.domain.model.WalkPoint;
import org.sopt.pawkey.backendapi.domain.routes.walk.domain.model.WalkSession;
import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.UserEntity;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RouteService {

	// [공간 데이터 SRID(좌표계) 불일치 에러 해결을 위한] 표준 위경도 좌표계(SRID 4326) 정보를 포함한 Geometry 생성 팩토리 설정
	private static final GeometryFactory GEOMETRY_FACTORY =
			new GeometryFactory(new PrecisionModel(), 4326);
	private final RouteRepository routeRepository;


	private LineString toLineString(List<WalkPoint> points) {

		if (points.size() < 2) {
			throw new RouteBusinessException(RouteErrorCode.INVALID_ROUTE_COORDINATES);
		}

		Coordinate[] coords = points.stream()
				.map(p -> new Coordinate(p.lng(), p.lat()))
				.toArray(Coordinate[]::new);

		return GEOMETRY_FACTORY.createLineString(coords);
	}




	public RouteEntity getRouteById(Long routeId) {
		return getRouteByRouteId(routeId)
			.orElseThrow(() -> new RouteBusinessException(RouteErrorCode.ROUTE_NOT_FOUND));
	}

	private Optional<RouteEntity> getRouteByRouteId(Long routeId) {
		return routeRepository.getRouteByRouteId(routeId);
	}


	@CacheEvict(value = "homeInfo", key = "#user.userId")
	public RouteEntity saveRouteFromSession(
			UserEntity user,
			SaveRouteCommand command,
			WalkSession session
	) {
		if (!session.isValid()) {
			throw new RouteBusinessException(RouteErrorCode.INVALID_ROUTE_COORDINATES);
		}
		LineString lineString = toLineString(session.getPoints());

		RouteEntity route = RouteEntity.builder()
				.user(user)
				.coordinates(lineString)
				.distance(command.distance())
				.duration(command.duration())
				.stepCount(command.stepCount())
				.region(user.getRegion())
				.startedAt(session.getStartedAt())
				.endedAt(command.endedAt())
				.build();

		return routeRepository.save(route);
	}

	public void delete(Long routeId) {
		routeRepository.deleteById(routeId);
	}

}
