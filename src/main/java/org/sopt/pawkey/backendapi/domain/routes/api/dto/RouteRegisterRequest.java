package org.sopt.pawkey.backendapi.domain.routes.api.dto;

import java.time.LocalDateTime;
import java.util.List;

import org.sopt.pawkey.backendapi.domain.routes.application.dto.command.RouteRegisterCommand;

public record RouteRegisterRequest(
	List<List<Double>> coordinates,
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
