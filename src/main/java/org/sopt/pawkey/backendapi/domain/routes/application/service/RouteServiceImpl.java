package org.sopt.pawkey.backendapi.domain.routes.application.service;

import java.util.List;

import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.sopt.pawkey.backendapi.domain.coordinate.Coordinate;
import org.sopt.pawkey.backendapi.domain.image.infra.persistence.entity.ImageEntity;
import org.sopt.pawkey.backendapi.domain.routes.application.dto.command.RouteRegisterCommand;
import org.sopt.pawkey.backendapi.domain.routes.domain.repository.RouteRepository;
import org.sopt.pawkey.backendapi.domain.routes.exception.RouteBusinessException;
import org.sopt.pawkey.backendapi.domain.routes.exception.RouteErrorCode;
import org.sopt.pawkey.backendapi.domain.routes.infra.persistence.entity.RouteEntity;
import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.UserEntity;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RouteServiceImpl implements RouteService {
	private static final GeometryFactory GEOMETRY_FACTORY = new GeometryFactory();
	private final RouteRepository routeRepository;

	@Override
	public RouteEntity getRouteById(Long routeId) {
		return routeRepository.getRouteByRouteId(routeId)
			.orElseThrow(() -> new RouteBusinessException(RouteErrorCode.ROUTE_NOT_FOUND));
	}

	@Override
	public RouteEntity saveRoute(UserEntity user, RouteRegisterCommand command, ImageEntity trackingImage) {
		LineString lineString = toLineString(command.coordinates());

		RouteEntity route = RouteEntity.builder()
			.user(user)
			.distance(command.distance())
			.duration(command.duration())
			.stepCount(command.stepCount())
			.region(user.getRegion())
			.trackingImage(trackingImage)
			.startedAt(command.startedAt())
			.coordinates(lineString)
			.endedAt(command.endedAt())
			.build();

		return routeRepository.save(route);
	}

	private static LineString toLineString(List<Coordinate> coordinates) {
		if (coordinates.size() < 2) {
			throw new RouteBusinessException(RouteErrorCode.INVALID_ROUTE_COORDINATES);
		}

		org.locationtech.jts.geom.Coordinate[] coords = coordinates.stream()
			.map(coord -> new org.locationtech.jts.geom.Coordinate(coord.longitude(), coord.latitude()))
			.toArray(org.locationtech.jts.geom.Coordinate[]::new);

		return GEOMETRY_FACTORY.createLineString(coords);
	}
}
