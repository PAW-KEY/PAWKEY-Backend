package org.sopt.pawkey.backendapi.domain.routes.application.service;

import java.util.List;
import java.util.Optional;

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
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RouteService {

	// [공간 데이터 SRID(좌표계) 불일치 에러 해결을 위한] 표준 위경도 좌표계(SRID 4326) 정보를 포함한 Geometry 생성 팩토리 설정
	private static final GeometryFactory GEOMETRY_FACTORY = new GeometryFactory(
		new org.locationtech.jts.geom.PrecisionModel(), 4326);
	private final RouteRepository routeRepository;

	private static LineString toLineString(List<Coordinate> coordinates) {

		if (coordinates.size() < 2) {
			throw new RouteBusinessException(RouteErrorCode.INVALID_ROUTE_COORDINATES);
		}

		org.locationtech.jts.geom.Coordinate[] coords = coordinates.stream()
			.map(coord -> new org.locationtech.jts.geom.Coordinate(coord.longitude(), coord.latitude()))
			.toArray(org.locationtech.jts.geom.Coordinate[]::new);

		LineString lineString = GEOMETRY_FACTORY.createLineString(coords);

		return lineString;
	}

	public RouteEntity getRouteById(Long routeId) {
		return getRouteByRouteId(routeId)
			.orElseThrow(() -> new RouteBusinessException(RouteErrorCode.ROUTE_NOT_FOUND));
	}

	@CacheEvict(value = "homeInfo", key = "#user.userId") // 홈 화면 속 산책 정보 조회를 위한 캐싱 삭제 코드
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

	private Optional<RouteEntity> getRouteByRouteId(Long routeId) {
		return routeRepository.getRouteByRouteId(routeId);
	}
}
