package org.sopt.pawkey.backendapi.domain.routes.api.dto;

import java.time.LocalDateTime;
import java.util.List;

import org.sopt.pawkey.backendapi.domain.coordinate.Coordinate;
import org.sopt.pawkey.backendapi.domain.routes.application.dto.command.RouteRegisterCommand;

import io.swagger.v3.oas.annotations.media.Schema;

public record RouteRegisterRequest(
	@Schema(description = "루트 좌표 리스트")
	List<Coordinate> coordinates,
	Double distance,
	int duration,
	LocalDateTime startedAt,
	LocalDateTime endedAt,
	int stepCount
) {
	public RouteRegisterCommand toCommand() {
		return RouteRegisterCommand.builder()
			.coordinates(coordinates())
			.distance(distance())
			.duration(duration())
			.startedAt(startedAt())
			.endedAt(endedAt())
			.stepCount(stepCount())
			.build();
	}

}
