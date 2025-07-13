package org.sopt.pawkey.backendapi.domain.routes.application.dto.command;

import java.time.LocalDateTime;
import java.util.List;

import org.sopt.pawkey.backendapi.domain.coordinate.Coordinate;

import lombok.Builder;

@Builder
public record RouteRegisterCommand(
	List<Coordinate> coordinates,
	int distance,
	int duration,
	LocalDateTime startedAt,
	LocalDateTime endedAt,
	int stepCount
) {

}
