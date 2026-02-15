package org.sopt.pawkey.backendapi.fixtures;

import java.time.LocalDateTime;
import java.util.List;

import org.sopt.pawkey.backendapi.domain.coordinate.Coordinate;
import org.sopt.pawkey.backendapi.domain.image.infra.persistence.entity.ImageEntity;
import org.sopt.pawkey.backendapi.domain.routes.application.dto.command.RouteRegisterCommand;

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
}