package org.sopt.pawkey.backendapi.domain.routes.application.dto.command;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;

@Builder
public record RouteRegisterCommand(
	List<List<Double>> coordinates,
	Double distance,
	int duration,
	LocalDateTime startedAt,
	LocalDateTime endedAt,
	int stepCount
) {
}
