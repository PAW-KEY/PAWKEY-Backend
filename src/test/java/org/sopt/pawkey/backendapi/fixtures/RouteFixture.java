package org.sopt.pawkey.backendapi.fixtures;

import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.List;

import org.locationtech.jts.geom.LineString;
import org.sopt.pawkey.backendapi.domain.coordinate.Coordinate;
import org.sopt.pawkey.backendapi.domain.image.infra.persistence.entity.ImageEntity;
import org.sopt.pawkey.backendapi.domain.region.infra.persistence.entity.RegionEntity;
import org.sopt.pawkey.backendapi.domain.routes.application.dto.command.RouteRegisterCommand;
import org.sopt.pawkey.backendapi.domain.routes.application.dto.command.SaveRouteCommand;
import org.sopt.pawkey.backendapi.domain.routes.infra.persistence.entity.RouteEntity;
import org.sopt.pawkey.backendapi.domain.routes.walk.domain.model.WalkPoint;
import org.sopt.pawkey.backendapi.domain.routes.walk.domain.model.WalkSession;
import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.UserEntity;

public class RouteFixture {

	public static RouteRegisterCommand createRegisterCommand() {
		List<Coordinate> testCoordinates = List.of(
			new Coordinate(37.5, 126.9),
			new Coordinate(37.51, 126.91)
		);

		return new RouteRegisterCommand(
			testCoordinates,
			2200,
			1200,
			LocalDateTime.now().minusMinutes(20),
			LocalDateTime.now(),
			1500,
				1L);
	}

	public static ImageEntity createRouteImage() {
		return ImageEntity.builder()
			.imageUrl("https://pawkey-s3.com/test-route-map.png")
			.width(1080)
			.height(720)
			.extension("png")
			.build();
	}

	public static SaveRouteCommand createSaveRouteCommand(Long userId, String routeId) {
		return new SaveRouteCommand(
			userId,
			routeId,
			2200,                          // distance
			1200,                          // duration
			1500,                          // stepCount
			LocalDateTime.now()            // endedAt
		);
	}

	public static WalkSession createValidSession(String routeId, Long userId) {
		WalkSession session = mock(WalkSession.class);

		lenient().when(session.isValid()).thenReturn(true);
		lenient().when(session.getPoints()).thenReturn(List.of(
			new WalkPoint(37.0, 127.0, 1L),
			new WalkPoint(37.1, 127.1, 2L)
		));

		return session;
	}


	public static RouteEntity createRoute(UserEntity user) {
		return RouteEntity.builder()
			.user(user)
			.distance(2200)
			.duration(1200)
			.stepCount(1500)
			.startedAt(LocalDateTime.now().minusMinutes(20))
			.endedAt(LocalDateTime.now())
			.coordinates(mock(LineString.class))
			.trackingImage(mock(ImageEntity.class))
			.region(mock(RegionEntity.class))
			.build();
	}

}